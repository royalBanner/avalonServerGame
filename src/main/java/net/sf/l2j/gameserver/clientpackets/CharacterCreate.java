package net.sf.l2j.gameserver.clientpackets;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.datatables.CharNameTable;
import net.sf.l2j.gameserver.datatables.CharTemplateTable;
import net.sf.l2j.gameserver.datatables.ItemTable;
import net.sf.l2j.gameserver.datatables.SkillTable;
import net.sf.l2j.gameserver.datatables.SkillTreeTable;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.L2ItemInstance;
import net.sf.l2j.gameserver.model.L2ShortCut;
import net.sf.l2j.gameserver.model.L2SkillLearn;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.network.L2GameClient;
import net.sf.l2j.gameserver.serverpackets.CharCreateFail;
import net.sf.l2j.gameserver.serverpackets.CharCreateOk;
import net.sf.l2j.gameserver.serverpackets.CharSelectInfo;
import net.sf.l2j.gameserver.templates.L2Item;
import net.sf.l2j.gameserver.templates.L2PcTemplate;
import net.sf.l2j.gameserver.util.Util;

/**
 * @version $Revision: 1.9.2.3.2.8 $ $Date: 2005/03/27 15:29:30 $
 */
@SuppressWarnings("unused")
public final class CharacterCreate extends L2GameClientPacket
{
	private static final String _C__0B_CHARACTERCREATE = "[C] 0B CharacterCreate";
	private static Logger _log = Logger.getLogger(CharacterCreate.class.getName());
	
	// cSdddddddddddd
	private String _name;
	private int _race;
	private byte _sex;
	private int _classId;
	private int _int;
	private int _str;
	private int _con;
	private int _men;
	private int _dex;
	private int _wit;
	private byte _hairStyle;
	private byte _hairColor;
	private byte _face;
	
	@Override
	protected void readImpl()
	{
		_name = readS();
		_race = readD();
		_sex = (byte) readD();
		_classId = readD();
		_int = readD();
		_str = readD();
		_con = readD();
		_men = readD();
		_dex = readD();
		_wit = readD();
		_hairStyle = (byte) readD();
		_hairColor = (byte) readD();
		_face = (byte) readD();
	}
	
	@Override
	protected void runImpl()
	{
		if ((CharNameTable.getInstance().accountCharNumber(getClient().getAccountName()) >= Config.MAX_CHARACTERS_NUMBER_PER_ACCOUNT) && (Config.MAX_CHARACTERS_NUMBER_PER_ACCOUNT != 0))
		{
			if (Config.DEBUG)
			{
				_log.fine("Max number of characters reached. Creation failed.");
			}
			CharCreateFail ccf = new CharCreateFail(CharCreateFail.REASON_TOO_MANY_CHARACTERS);
			sendPacket(ccf);
			return;
		}
		else if (CharNameTable.getInstance().doesCharNameExist(_name))
		{
			if (Config.DEBUG)
			{
				_log.fine("charname: " + _name + " already exists. creation failed.");
			}
			CharCreateFail ccf = new CharCreateFail(CharCreateFail.REASON_NAME_ALREADY_EXISTS);
			sendPacket(ccf);
			return;
		}
		else if ((_name.length() < 3) || (_name.length() > 16) || !Util.isAlphaNumeric(_name) || !isValidName(_name))
		{
			if (Config.DEBUG)
			{
				_log.fine("charname: " + _name + " is invalid. creation failed.");
			}
			CharCreateFail ccf = new CharCreateFail(CharCreateFail.REASON_16_ENG_CHARS);
			sendPacket(ccf);
			return;
		}
		
		if (Config.DEBUG)
		{
			_log.fine("charname: " + _name + " classId: " + _classId);
		}
		
		L2PcTemplate template = CharTemplateTable.getInstance().getTemplate(_classId);
		if ((template == null) || (template.classBaseLevel > 1))
		{
			CharCreateFail ccf = new CharCreateFail(CharCreateFail.REASON_CREATION_FAILED);
			sendPacket(ccf);
			return;
		}
		
		int objectId = IdFactory.getInstance().getNextId();
		L2PcInstance newChar = L2PcInstance.create(objectId, template, getClient().getAccountName(), _name, _hairStyle, _hairColor, _face, _sex != 0);
		newChar.setCurrentHp(template.baseHpMax);
		newChar.setCurrentCp(template.baseCpMax);
		newChar.setCurrentMp(template.baseMpMax);
		// newChar.setMaxLoad(template.baseLoad);
		
		// send acknowledgement
		CharCreateOk cco = new CharCreateOk();
		sendPacket(cco);
		
		initNewChar(getClient(), newChar);
	}
	
	private boolean isValidName(String text)
	{
		boolean result = true;
		String test = text;
		Pattern pattern;
		try
		{
			pattern = Pattern.compile(Config.CNAME_TEMPLATE);
		}
		catch (PatternSyntaxException e) // case of illegal pattern
		{
			_log.warning("ERROR : Character name pattern of config is wrong!");
			pattern = Pattern.compile(".*");
		}
		Matcher regexp = pattern.matcher(test);
		if (!regexp.matches())
		{
			result = false;
		}
		return result;
	}
	
	private void initNewChar(L2GameClient client, L2PcInstance newChar)
	{
		if (Config.DEBUG)
		{
			_log.fine("Character init start");
		}
		L2World.getInstance().storeObject(newChar);
		
		L2PcTemplate template = newChar.getTemplate();
		
		newChar.addAdena("Init", Config.STARTING_ADENA, null, false);
		
		newChar.setXYZInvisible(template.spawnX, template.spawnY, template.spawnZ);
		newChar.setTitle("");
		
		L2ShortCut shortcut;
		// add attack shortcut
		shortcut = new L2ShortCut(0, 0, 3, 2, -1, 1);
		newChar.registerShortCut(shortcut);
		// add take shortcut
		shortcut = new L2ShortCut(3, 0, 3, 5, -1, 1);
		newChar.registerShortCut(shortcut);
		// add sit shortcut
		shortcut = new L2ShortCut(10, 0, 3, 0, -1, 1);
		newChar.registerShortCut(shortcut);
		
		ItemTable.getInstance();
		L2Item[] items = template.getItems();
		for (L2Item item2 : items)
		{
			L2ItemInstance item = newChar.getInventory().addItem("Init", item2.getItemId(), 1, newChar, null);
			if (item.getItemId() == 5588)
			{
				// add tutbook shortcut
				shortcut = new L2ShortCut(11, 0, 1, item.getObjectId(), -1, 1);
				newChar.registerShortCut(shortcut);
			}
			if (item.isEquipable())
			{
				if ((newChar.getActiveWeaponItem() == null) || !(item.getItem().getType2() != L2Item.TYPE2_WEAPON))
				{
					newChar.getInventory().equipItemAndRecord(item);
				}
			}
		}
		
		L2SkillLearn[] startSkills = SkillTreeTable.getInstance().getAvailableSkills(newChar, newChar.getClassId());
		for (L2SkillLearn startSkill : startSkills)
		{
			newChar.addSkill(SkillTable.getInstance().getInfo(startSkill.getId(), startSkill.getLevel()), true);
			if ((startSkill.getId() == 1001) || (startSkill.getId() == 1177))
			{
				shortcut = new L2ShortCut(1, 0, 2, startSkill.getId(), 1, 1);
				newChar.registerShortCut(shortcut);
			}
			if (startSkill.getId() == 1216)
			{
				shortcut = new L2ShortCut(10, 0, 2, startSkill.getId(), 1, 1);
				newChar.registerShortCut(shortcut);
			}
			if (Config.DEBUG)
			{
				_log.fine("adding starter skill:" + startSkill.getId() + " / " + startSkill.getLevel());
			}
		}
		
		L2GameClient.saveCharToDisk(newChar);
		newChar.deleteMe(); // release the world of this character and it's inventory
		
		// send char list
		
		CharSelectInfo cl = new CharSelectInfo(client.getAccountName(), client.getSessionId().playOkID1);
		client.getConnection().sendPacket(cl);
		client.setCharSelection(cl.getCharInfo());
		if (Config.DEBUG)
		{
			_log.fine("Character init end");
		}
	}
	
	@Override
	public String getType()
	{
		return _C__0B_CHARACTERCREATE;
	}
}