/*
 * Copyright © 2004-2020 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.script.faenor;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.GameServer;
import net.sf.l2j.gameserver.script.Parser;
import net.sf.l2j.gameserver.script.ParserNotCreatedException;
import net.sf.l2j.gameserver.script.ScriptDocument;
import net.sf.l2j.gameserver.script.ScriptEngine;
import net.sf.l2j.gameserver.script.ScriptPackage;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.w3c.dom.Node;

/**
 * @author Luis Arias
 */
public class FaenorScriptEngine extends ScriptEngine
{
	static Logger _log = Logger.getLogger(GameServer.class.getName());
	public final static String PACKAGE_DIRECTORY = "data/script/";
	public final static boolean DEBUG = true;
	
	private static FaenorScriptEngine _instance;
	
	private LinkedList<ScriptDocument> _scripts;
	
	public static FaenorScriptEngine getInstance()
	{
		if (_instance == null)
		{
			_instance = new FaenorScriptEngine();
		}
		
		return _instance;
	}
	
	private FaenorScriptEngine()
	{
		_scripts = new LinkedList<>();
		loadPackages();
		parsePackages();
		
	}
	
	public void reloadPackages()
	{
		_scripts = new LinkedList<>();
		parsePackages();
	}
	
	private void loadPackages()
	{
		File packDirectory = new File(Config.DATAPACK_ROOT, PACKAGE_DIRECTORY);// _log.sss(packDirectory.getAbsolutePath());
		
		FileFilter fileFilter = file -> file.getName().endsWith(".zip");
		
		File[] files = packDirectory.listFiles(fileFilter);
		if (files == null)
		{
			return;
		}
		ZipFile zipPack;
		
		for (File file : files)
		{
			try
			{
				zipPack = new ZipFile(file);
			}
			catch (ZipException e)
			{
				e.printStackTrace();
				continue;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				continue;
			}
			
			ScriptPackage module = new ScriptPackage(zipPack);
			
			List<ScriptDocument> scrpts = module.getScriptFiles();
			for (ScriptDocument script : scrpts)
			{
				_scripts.add(script);
			}
			
		}
		/*
		 * for (ScriptDocument script : scripts) { _log.sss("Script: "+script); } _log.sss("Sorting"); orderScripts(); for (ScriptDocument script : scripts) { _log.sss("Script: "+script); }
		 */
	}
	
	public void orderScripts()
	{
		if (_scripts.size() > 1)
		{
			// ScriptDocument npcInfo = null;
			
			for (int i = 0; i < _scripts.size();)
			{
				if (_scripts.get(i).getName().contains("NpcStatData"))
				{
					_scripts.addFirst(_scripts.remove(i));
					// scripts.set(i, scripts.get(0));
					// scripts.set(0, npcInfo);
				}
				else
				{
					i++;
				}
			}
		}
	}
	
	public void parsePackages()
	{
		BSFManager context = new BSFManager();
		try
		{
			context.eval("beanshell", "core", 0, 0, "double log1p(double d) { return Math.log1p(d); }");
			context.eval("beanshell", "core", 0, 0, "double pow(double d, double p) { return Math.pow(d,p); }");
			
			for (ScriptDocument script : _scripts)
			{
				parseScript(script, context);
				// System.out.println(script.getName());
			}
		}
		catch (BSFException e)
		{
			e.printStackTrace();
		}
	}
	
	public void parseScript(ScriptDocument script, BSFManager context)
	{
		if (DEBUG)
		{
			_log.fine("Parsing Script: " + script.getName());
		}
		
		Node node = script.getDocument().getFirstChild();
		String parserClass = "faenor.Faenor" + node.getNodeName() + "Parser";
		
		Parser parser = null;
		try
		{
			parser = createParser(parserClass);
		}
		catch (ParserNotCreatedException e)
		{
			_log.warning("ERROR: No parser registered for Script: " + parserClass);
			e.printStackTrace();
		}
		
		if (parser == null)
		{
			_log.warning("Unknown Script Type: " + script.getName());
			return;
		}
		
		try
		{
			parser.parseScript(node, context);
			_log.fine(script.getName() + "Script Sucessfullty Parsed.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			_log.warning("Script Parsing Failed.");
		}
	}
	
	@Override
	public String toString()
	{
		if (_scripts.isEmpty())
		{
			return "No Packages Loaded.";
		}
		
		String out = "Script Packages currently loaded:\n";
		
		for (ScriptDocument script : _scripts)
		{
			out += script;
		}
		return out;
	}
}
