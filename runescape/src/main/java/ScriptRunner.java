import com.jagex.Client;
import com.jagex.ClientProt;
import com.jagex.FullscreenMode;
import com.jagex.PrivateChatMode;
import com.jagex.core.constants.MainLogicStep;
import com.jagex.core.constants.MiniMenuAction;
import com.jagex.core.constants.ModeWhat;
import com.jagex.core.constants.WindowMode;
import com.jagex.core.datastruct.key.IntNode;
import com.jagex.core.datastruct.key.IterableHashTable;
import com.jagex.core.datastruct.key.Node;
import com.jagex.core.datastruct.key.Queue;
import com.jagex.core.datastruct.ref.ReferenceCache;
import com.jagex.core.io.ConnectionInfo;
import com.jagex.core.io.Packet;
import com.jagex.core.stringtools.general.NameTools;
import com.jagex.core.stringtools.general.StringTools;
import com.jagex.core.stringtools.general.WebTools;
import com.jagex.core.util.Arrays;
import com.jagex.core.util.JagException;
import com.jagex.core.util.JavaScript;
import com.jagex.core.util.SystemTimer;
import com.jagex.core.util.TimeUtils;
import com.jagex.game.DelayedStateChange;
import com.jagex.game.LocalisedText;
import com.jagex.game.PlayerModel;
import com.jagex.game.camera.CameraMode;
import com.jagex.game.compression.huffman.WordPack;
import com.jagex.game.runetek6.client.GameShell;
import com.jagex.game.runetek6.config.bastype.BASType;
import com.jagex.game.runetek6.config.bastype.BASTypeList;
import com.jagex.game.runetek6.config.defaults.AudioDefaults;
import com.jagex.game.runetek6.config.enumtype.EnumMapping;
import com.jagex.game.runetek6.config.enumtype.EnumStringMapping;
import com.jagex.game.runetek6.config.enumtype.EnumType;
import com.jagex.game.runetek6.config.enumtype.EnumTypeList;
import com.jagex.game.runetek6.config.idktype.IDKTypeList;
import com.jagex.game.runetek6.config.iftype.DragRenderBehaviour;
import com.jagex.game.runetek6.config.iftype.ServerActiveProperties;
import com.jagex.game.runetek6.config.iftype.SubInterface;
import com.jagex.game.runetek6.config.invtype.InvTypeList;
import com.jagex.game.runetek6.config.loctype.LocTypeList;
import com.jagex.game.runetek6.config.meltype.MapElementType;
import com.jagex.game.runetek6.config.meltype.MapElementTypeList;
import com.jagex.game.runetek6.config.npctype.NPCTypeList;
import com.jagex.game.runetek6.config.objtype.ObjNumMode;
import com.jagex.game.runetek6.config.objtype.ObjType;
import com.jagex.game.runetek6.config.objtype.ObjTypeList;
import com.jagex.game.runetek6.config.paramtype.ParamType;
import com.jagex.game.runetek6.config.paramtype.ParamTypeList;
import com.jagex.game.runetek6.config.quickchatcattype.QuickChatCatType;
import com.jagex.game.runetek6.config.quickchatcattype.QuickChatCatTypeList;
import com.jagex.game.runetek6.config.structtype.StructTypeList;
import com.jagex.game.runetek6.config.vartype.TimedVarDomain;
import com.jagex.game.runetek6.config.vartype.clan.VarClanSettingType;
import com.jagex.game.runetek6.config.vartype.clan.VarClanSettingTypeList;
import com.jagex.game.runetek6.config.vartype.clan.VarClanType;
import com.jagex.game.runetek6.config.vartype.clan.VarClanTypeList;
import com.jagex.game.world.Country;
import com.jagex.game.world.GameWorld;
import com.jagex.graphics.FontMetrics;
import com.jagex.graphics.HorizontalAlignment;
import com.jagex.graphics.Toolkit;
import com.jagex.graphics.ToolkitType;
import com.jagex.graphics.VerticalAlignment;
import com.jagex.js5.js5;
import com.jagex.math.ColourUtils;
import com.jagex.trigger.ClientTriggerType;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;
import rs2.client.clan.channel.ClanChannel;
import rs2.client.clan.settings.ClanSettings;
import rs2.client.event.keyboard.KeyboardMonitor;
import rs2.client.event.keyboard.SimpleKeyboardMonitor;
import rs2.client.event.mouse.MouseMonitor;
import rs2.client.loading.library.LibraryManager;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECHATPHRASE_PREPARE;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECHATPHRASE_SENDCLAN;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECHATPHRASE_SENDPRIVATE;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECHATPHRASE_SENDPUBLIC;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECHATPHRASE_SETDYNAMICINT;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECHATPHRASE_SETDYNAMICOBJ;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANCHANNEL_FIND_AFFINED;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANCHANNEL_FIND_LISTENED;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANCHANNEL_GETCLANNAME;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANCHANNEL_GETRANKKICK;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANCHANNEL_GETRANKTALK;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANCHANNEL_GETSORTEDUSERSLOT;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANCHANNEL_GETUSERCOUNT;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANCHANNEL_GETUSERDISPLAYNAME;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANCHANNEL_GETUSERRANK;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANCHANNEL_GETUSERSLOT;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANCHANNEL_GETUSERWORLD;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANCHANNEL_KICKUSER;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANSETTINGS_FIND_AFFINED;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANSETTINGS_GETAFFINEDCOUNT;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANSETTINGS_GETAFFINEDDISPLAYNAME;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANSETTINGS_GETAFFINEDEXTRAINFO;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANSETTINGS_GETAFFINEDJOINRUNEDAY;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANSETTINGS_GETAFFINEDRANK;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANSETTINGS_GETAFFINEDSLOT;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANSETTINGS_GETALLOWUNAFFINED;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANSETTINGS_GETBANNEDCOUNT;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANSETTINGS_GETBANNEDDISPLAYNAME;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANSETTINGS_GETCLANNAME;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANSETTINGS_GETCOINSHARE;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANSETTINGS_GETCURRENTOWNER_SLOT;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANSETTINGS_GETRANKKICK;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANSETTINGS_GETRANKLOOTSHARE;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANSETTINGS_GETRANKTALK;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANSETTINGS_GETREPLACEMENTOWNER_SLOT;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVECLANSETTINGS_GETSORTEDAFFINEDSLOT;
import static com.jagex.core.constants.ClientScriptOpCode.ACTIVELCANSETTINGS_FIND_LISTENED;
import static com.jagex.core.constants.ClientScriptOpCode.ADD;
import static com.jagex.core.constants.ClientScriptOpCode.ADDPERCENT;
import static com.jagex.core.constants.ClientScriptOpCode.AFFILIATE;
import static com.jagex.core.constants.ClientScriptOpCode.AFFINEDCLANSETTINGS_ADDBANNED_FROMCHANNEL;
import static com.jagex.core.constants.ClientScriptOpCode.AND;
import static com.jagex.core.constants.ClientScriptOpCode.APPEND;
import static com.jagex.core.constants.ClientScriptOpCode.APPEND_CHAR;
import static com.jagex.core.constants.ClientScriptOpCode.APPEND_NUM;
import static com.jagex.core.constants.ClientScriptOpCode.APPEND_SIGNNUM;
import static com.jagex.core.constants.ClientScriptOpCode.APPLET_HASFOCUS;
import static com.jagex.core.constants.ClientScriptOpCode.AUTOSETUP_BLACKFLAGLAST;
import static com.jagex.core.constants.ClientScriptOpCode.AUTOSETUP_DOSETUP;
import static com.jagex.core.constants.ClientScriptOpCode.AUTOSETUP_GETLEVEL;
import static com.jagex.core.constants.ClientScriptOpCode.AUTOSETUP_SETCUSTOM;
import static com.jagex.core.constants.ClientScriptOpCode.AUTOSETUP_SETHIGH;
import static com.jagex.core.constants.ClientScriptOpCode.AUTOSETUP_SETLOW;
import static com.jagex.core.constants.ClientScriptOpCode.AUTOSETUP_SETMEDIUM;
import static com.jagex.core.constants.ClientScriptOpCode.AUTOSETUP_SETMIN;
import static com.jagex.core.constants.ClientScriptOpCode.BASECOLOUR;
import static com.jagex.core.constants.ClientScriptOpCode.BASEIDKIT;
import static com.jagex.core.constants.ClientScriptOpCode.BAS_GETANIM_READY;
import static com.jagex.core.constants.ClientScriptOpCode.BRANCH;
import static com.jagex.core.constants.ClientScriptOpCode.BRANCH_EQUALS;
import static com.jagex.core.constants.ClientScriptOpCode.BRANCH_GREATER_THAN;
import static com.jagex.core.constants.ClientScriptOpCode.BRANCH_GREATER_THAN_OR_EQUALS;
import static com.jagex.core.constants.ClientScriptOpCode.BRANCH_IF_FALSE;
import static com.jagex.core.constants.ClientScriptOpCode.BRANCH_IF_TRUE;
import static com.jagex.core.constants.ClientScriptOpCode.BRANCH_LESS_THAN;
import static com.jagex.core.constants.ClientScriptOpCode.BRANCH_LESS_THAN_OR_EQUALS;
import static com.jagex.core.constants.ClientScriptOpCode.BRANCH_NOT;
import static com.jagex.core.constants.ClientScriptOpCode.CAM_DEC_X;
import static com.jagex.core.constants.ClientScriptOpCode.CAM_DEC_Y;
import static com.jagex.core.constants.ClientScriptOpCode.CAM_FOLLOWCOORD;
import static com.jagex.core.constants.ClientScriptOpCode.CAM_FORCEANGLE;
import static com.jagex.core.constants.ClientScriptOpCode.CAM_GETANGLE_XA;
import static com.jagex.core.constants.ClientScriptOpCode.CAM_GETANGLE_YA;
import static com.jagex.core.constants.ClientScriptOpCode.CAM_INC_X;
import static com.jagex.core.constants.ClientScriptOpCode.CAM_INC_Y;
import static com.jagex.core.constants.ClientScriptOpCode.CAM_LOOKAT;
import static com.jagex.core.constants.ClientScriptOpCode.CAM_MOVEALONG;
import static com.jagex.core.constants.ClientScriptOpCode.CAM_MOVETO;
import static com.jagex.core.constants.ClientScriptOpCode.CAM_REMOVEROOF;
import static com.jagex.core.constants.ClientScriptOpCode.CAM_RESET;
import static com.jagex.core.constants.ClientScriptOpCode.CAM_SMOOTH_RESET;
import static com.jagex.core.constants.ClientScriptOpCode.CAN_RUN_JAVA_CLIENT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_CREATE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_DELETE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_DELETEALL;
import static com.jagex.core.constants.ClientScriptOpCode.CC_DRAGPICKUP;
import static com.jagex.core.constants.ClientScriptOpCode.CC_FIND;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GET2DANGLE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETCOLOUR;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETFONTGRAPHIC;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETGRAPHIC;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETHEIGHT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETHIDE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETID;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETINVCOUNT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETINVOBJECT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETLAYER;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETMODEL;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETMODELANGLE_X;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETMODELANGLE_Y;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETMODELANGLE_Z;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETMODELXOF;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETMODELYOF;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETMODELZOOM;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETOP;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETOPBASE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETPARENTLAYER;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETSCROLLHEIGHT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETSCROLLWIDTH;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETSCROLLX;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETSCROLLY;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETTARGETMASK;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETTEXT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETTRANS;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETWIDTH;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETX;
import static com.jagex.core.constants.ClientScriptOpCode.CC_GETY;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_CALLONRESIZE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_CLEAROPS;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_CLEARSCRIPTHOOKS;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SET2DANGLE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETALPHA;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETASPECT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETCLICKMASK;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETCOLOUR;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETDRAGDEADTIME;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETDRAGDEADZONE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETDRAGGABLE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETDRAGRENDERBEHAVIOUR;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETFILL;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETFONTMONO;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETGRAPHIC;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETGRAPHICSHADOW;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETHFLIP;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETHIDE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETLINEDIRECTION;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETLINEWID;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETMAXLINES;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETMODEL;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETMODELANGLE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETMODELANIM;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETMODELORIGIN;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETMODELORTHOG;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETMODELZOOM;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETMOUSEOVERCURSOR;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETNOCLICKTHROUGH;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETNPCHEAD;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETNPCMODEL;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETOBJECT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETOBJECT_ALWAYSNUM;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETOBJECT_DATA;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETOBJECT_NONUM;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETOBJECT_WEARCOL;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETOBJECT_WEARCOL_ALWAYSNUM;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETOBJECT_WEARCOL_NONUM;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONCAMFINISHED;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONCHATTRANSMIT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONCLANCHANNELTRANSMIT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONCLANSETTINGSTRANSMIT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONCLANTRANSMIT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONCLICK;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONCLICKREPEAT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONDIALOGABORT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONDRAG;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONDRAGCOMPLETE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONFRIENDTRANSMIT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONHOLD;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONINVTRANSMIT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONKEY;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONMISCTRANSMIT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONMOUSELEAVE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONMOUSEOVER;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONMOUSEREPEAT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONOP;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONOPT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONRELEASE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONRESIZE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONSCROLLWHEEL;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONSTATTRANSMIT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONSTOCKTRANSMIT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONSUBCHANGE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONTARGETENTER;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONTARGETLEAVE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONTIMER;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONVARCLANTRANSMIT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONVARCSTRTRANSMIT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONVARCTRANSMIT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETONVARTRANSMIT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETOP;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETOPBASE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETOPCHAR;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETOPCURSOR;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETOPKEY;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETOUTLINE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETPARAM;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETPARAM_INT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETPARAM_STRING;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETPAUSETEXT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETPLAYERHEAD_SELF;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETPLAYERMODEL;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETPLAYERMODEL_SELF;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETPOSITION;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETRECOL;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETRETEX;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETSCROLLPOS;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETSCROLLSIZE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETSIZE;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETTARGETCURSORS;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETTARGETOPCURSOR;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETTARGETVERB;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETTEXT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETTEXTALIGN;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETTEXTFONT;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETTEXTSHADOW;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETTILING;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETTTRANS;
import static com.jagex.core.constants.ClientScriptOpCode.CC_IF_SETVFLIP;
import static com.jagex.core.constants.ClientScriptOpCode.CC_PARAM;
import static com.jagex.core.constants.ClientScriptOpCode.CC_SENDTOBACK;
import static com.jagex.core.constants.ClientScriptOpCode.CC_SENDTOFRONT;
import static com.jagex.core.constants.ClientScriptOpCode.CHAR_ISALPHA;
import static com.jagex.core.constants.ClientScriptOpCode.CHAR_ISALPHANUMERIC;
import static com.jagex.core.constants.ClientScriptOpCode.CHAR_ISNUMERIC;
import static com.jagex.core.constants.ClientScriptOpCode.CHAR_ISPRINTABLE;
import static com.jagex.core.constants.ClientScriptOpCode.CHAR_TOLOWERCASE;
import static com.jagex.core.constants.ClientScriptOpCode.CHAR_TOUPPERCASE;
import static com.jagex.core.constants.ClientScriptOpCode.CHATCAT_FINDPHRASEBYSHORTCUT;
import static com.jagex.core.constants.ClientScriptOpCode.CHATCAT_FINDSUBCATBYSHORTCUT;
import static com.jagex.core.constants.ClientScriptOpCode.CHATCAT_GETDESC;
import static com.jagex.core.constants.ClientScriptOpCode.CHATCAT_GETPHRASE;
import static com.jagex.core.constants.ClientScriptOpCode.CHATCAT_GETPHRASECOUNT;
import static com.jagex.core.constants.ClientScriptOpCode.CHATCAT_GETPHRASESHORTCUT;
import static com.jagex.core.constants.ClientScriptOpCode.CHATCAT_GETSUBCAT;
import static com.jagex.core.constants.ClientScriptOpCode.CHATCAT_GETSUBCATCOUNT;
import static com.jagex.core.constants.ClientScriptOpCode.CHATCAT_GETSUBCATSHORTCUT;
import static com.jagex.core.constants.ClientScriptOpCode.CHATPHRASE_FIND;
import static com.jagex.core.constants.ClientScriptOpCode.CHATPHRASE_FINDNEXT;
import static com.jagex.core.constants.ClientScriptOpCode.CHATPHRASE_FINDRESTART;
import static com.jagex.core.constants.ClientScriptOpCode.CHATPHRASE_GETAUTORESPONSE;
import static com.jagex.core.constants.ClientScriptOpCode.CHATPHRASE_GETAUTORESPONSECOUNT;
import static com.jagex.core.constants.ClientScriptOpCode.CHATPHRASE_GETDYNAMICCOMMAND;
import static com.jagex.core.constants.ClientScriptOpCode.CHATPHRASE_GETDYNAMICCOMMANDCOUNT;
import static com.jagex.core.constants.ClientScriptOpCode.CHATPHRASE_GETDYNAMICCOMMANDPARAM_ENUM;
import static com.jagex.core.constants.ClientScriptOpCode.CHATPHRASE_GETTEXT;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_GETFILTER_PRIVATE;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_GETFILTER_PUBLIC;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_GETFILTER_TRADE;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_GETHISTORYCLAN;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_GETHISTORYDISPLAYNAME;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_GETHISTORYLENGTH;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_GETHISTORYNAME;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_GETHISTORYNAME_UNFILTERED;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_GETHISTORYPHRASE;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_GETHISTORY_BYTYPEANDLINE;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_GETHISTORY_BYUID;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_GETNEXTUID;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_GETPREVUID;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_PLAYERNAME;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_PLAYERNAME_UNFILTERED;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_SENDABUSEREPORT;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_SENDPRIVATE;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_SENDPUBLIC;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_SETFILTER;
import static com.jagex.core.constants.ClientScriptOpCode.CHAT_SETMODE;
import static com.jagex.core.constants.ClientScriptOpCode.CLANFORUMQFC_TOSTRING;
import static com.jagex.core.constants.ClientScriptOpCode.CLANPROFILE_FIND;
import static com.jagex.core.constants.ClientScriptOpCode.CLAN_GETCHATCOUNT;
import static com.jagex.core.constants.ClientScriptOpCode.CLAN_GETCHATDISPLAYNAME;
import static com.jagex.core.constants.ClientScriptOpCode.CLAN_GETCHATMINKICK;
import static com.jagex.core.constants.ClientScriptOpCode.CLAN_GETCHATOWNERNAME;
import static com.jagex.core.constants.ClientScriptOpCode.CLAN_GETCHATRANK;
import static com.jagex.core.constants.ClientScriptOpCode.CLAN_GETCHATUSERNAME;
import static com.jagex.core.constants.ClientScriptOpCode.CLAN_GETCHATUSERNAME_UNFILTERED;
import static com.jagex.core.constants.ClientScriptOpCode.CLAN_GETCHATUSERRANK;
import static com.jagex.core.constants.ClientScriptOpCode.CLAN_GETCHATUSERWORLD;
import static com.jagex.core.constants.ClientScriptOpCode.CLAN_GETCHATUSERWORLDNAME;
import static com.jagex.core.constants.ClientScriptOpCode.CLAN_ISSELF;
import static com.jagex.core.constants.ClientScriptOpCode.CLAN_JOINCHAT;
import static com.jagex.core.constants.ClientScriptOpCode.CLAN_KICKUSER;
import static com.jagex.core.constants.ClientScriptOpCode.CLAN_LEAVECHAT;
import static com.jagex.core.constants.ClientScriptOpCode.CLEARBIT;
import static com.jagex.core.constants.ClientScriptOpCode.CLIENTCLOCK;
import static com.jagex.core.constants.ClientScriptOpCode.COMLEVEL_ACTIVE;
import static com.jagex.core.constants.ClientScriptOpCode.COMPARE;
import static com.jagex.core.constants.ClientScriptOpCode.COORD;
import static com.jagex.core.constants.ClientScriptOpCode.COORDX;
import static com.jagex.core.constants.ClientScriptOpCode.COORDY;
import static com.jagex.core.constants.ClientScriptOpCode.COORDZ;
import static com.jagex.core.constants.ClientScriptOpCode.CREATE_AVAILABLEREQUEST;
import static com.jagex.core.constants.ClientScriptOpCode.CREATE_CONNECTREQUEST;
import static com.jagex.core.constants.ClientScriptOpCode.CREATE_REPLY;
import static com.jagex.core.constants.ClientScriptOpCode.CREATE_SETUNDER13;
import static com.jagex.core.constants.ClientScriptOpCode.CREATE_UNDER13;
import static com.jagex.core.constants.ClientScriptOpCode.DATE_ISLEAPYEAR;
import static com.jagex.core.constants.ClientScriptOpCode.DATE_MINUTES;
import static com.jagex.core.constants.ClientScriptOpCode.DATE_MINUTES_FROMRUNEDAY;
import static com.jagex.core.constants.ClientScriptOpCode.DATE_RUNEDAY;
import static com.jagex.core.constants.ClientScriptOpCode.DATE_RUNEDAY_FROMDATE;
import static com.jagex.core.constants.ClientScriptOpCode.DATE_RUNEDAY_TODATE;
import static com.jagex.core.constants.ClientScriptOpCode.DATE_YEAR;
import static com.jagex.core.constants.ClientScriptOpCode.DEFAULTMINIMENU;
import static com.jagex.core.constants.ClientScriptOpCode.DEFINE_ARRAY;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANMOD_ANTIALIASING;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANMOD_BLOOM;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANMOD_BUILDAREA;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANMOD_FOG;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANMOD_GROUNDBLENDING;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANMOD_GROUNDDECOR;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANMOD_HARDSHADOWS;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANMOD_MAXSCREENSIZE;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANMOD_ORTHOGRAPHIC;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANMOD_PARTICLES;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANMOD_SKYDETAIL;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANMOD_SPOTSHADOWS;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANMOD_TEXTURING;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANMOD_TOOLKIT_DEFAULT;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANMOD_WATERDETAIL;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANSET_ANTIALIASING;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANSET_BLOOM;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANSET_BUILDAREA;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANSET_FOG;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANSET_GROUNDBLENDING;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANSET_GROUNDDECOR;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANSET_HARDSHADOWS;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANSET_MAXSCREENSIZE;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANSET_ORTHOGRAPHIC;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANSET_PARTICLES;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANSET_SKYDETAIL;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANSET_SPOTSHADOWS;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANSET_TEXTURING;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANSET_TOOLKIT_DEFAULT;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILCANSET_WATERDETAIL;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_ANIMDETAIL;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_ANTIALIASING;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_ANTIALIASING_QUALITY;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_BGSOUNDVOL;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_BLOOM;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_BRIGHTNESS;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_BUILDAREA;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_CANCHOOSESAFEMODE;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_CHOSESAFEMODE;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_CPUUSAGE;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_CUSTOMCURSORS;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_FLICKERING_ON;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_FOG_ON;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_GROUNDBLENDING;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_GROUNDDECOR_ON;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_HARDSHADOWS;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_IDLEANIMS;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_IDLEANIMS_MANY;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_LIGHTDETAIL_HIGH;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_LOADINGSCREENTYPE;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_LOGINVOL;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_MAXSCREENSIZE;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_MUSICVOL;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_ORTHOGRAPHIC;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_PARTICLES;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_PERFORMANCE_METRIC;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_REMOVEROOFS_OPTIONS;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_SAFEMODE;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_SKYDETAIL;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_SOUNDVOL;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_SPEECHVOL;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_SPOTSHADOWS;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_STEREO;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_TEXTURING;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_TOOLKIT;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_TOOLKIT_DEFAULT;
import static com.jagex.core.constants.ClientScriptOpCode.DETAILGET_WATERDETAIL_HIGH;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_ANIMDETAIL;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_ANTIALIASING_DEFAULT;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_ANTIALIASING_QUALITY;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_BGSOUNDVOL;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_BLOOM;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_BRIGHTNESS;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_BUILDAREA;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_CPUUSAGE;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_CUSTOMCURSORS;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_FLICKERING_ON;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_FOG_ON;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_GROUNDBLOUNDING;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_GROUNDDECOR_ON;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_HARDSHADOWS;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_IDLEANIMS;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_IDLEANIMS_MANY;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_LIGHTDETAIL_HIGH;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_LOADINGSCREENTYPE;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_LOGINVOL;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_MAXSCREENSIZE;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_MUSICVOL;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_ORTHOGRAPHIC;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_PARTICLES;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_REMOVEROOFS_OPTION;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_REMOVEROOFS_OPTION_OVERRIDE;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_SKYDETAIL;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_SOUNDVOL;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_SPEECHVOL;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_SPOTSHADOWS_ON;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_STEREO;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_TEXTURING;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_TOOLKIT;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_TOOLKIT_DEFAULT;
import static com.jagex.core.constants.ClientScriptOpCode.DETAIL_WATERDETAIL_HIGH;
import static com.jagex.core.constants.ClientScriptOpCode.DIVIDE;
import static com.jagex.core.constants.ClientScriptOpCode.DOCHEAT;
import static com.jagex.core.constants.ClientScriptOpCode.ENUM;
import static com.jagex.core.constants.ClientScriptOpCode.ENUM_GETOUTPUT_COUNT;
import static com.jagex.core.constants.ClientScriptOpCode.ENUM_GETREVERSECOUNT;
import static com.jagex.core.constants.ClientScriptOpCode.ENUM_GETREVERSECOUNT_STRING;
import static com.jagex.core.constants.ClientScriptOpCode.ENUM_GETREVERSEINDEX;
import static com.jagex.core.constants.ClientScriptOpCode.ENUM_GETREVERSEINDEX_STRING;
import static com.jagex.core.constants.ClientScriptOpCode.ENUM_HASOUTPUT;
import static com.jagex.core.constants.ClientScriptOpCode.ENUM_HASOUTPUT_STRING;
import static com.jagex.core.constants.ClientScriptOpCode.ENUM_STRING;
import static com.jagex.core.constants.ClientScriptOpCode.ESCAPE;
import static com.jagex.core.constants.ClientScriptOpCode.FORMATMINIMENU;
import static com.jagex.core.constants.ClientScriptOpCode.FORMAT_DATETIME_FROM_MINUTES;
import static com.jagex.core.constants.ClientScriptOpCode.FRIEND_ADD;
import static com.jagex.core.constants.ClientScriptOpCode.FRIEND_COUNT;
import static com.jagex.core.constants.ClientScriptOpCode.FRIEND_DEL;
import static com.jagex.core.constants.ClientScriptOpCode.FRIEND_GETNAME;
import static com.jagex.core.constants.ClientScriptOpCode.FRIEND_GETRANK;
import static com.jagex.core.constants.ClientScriptOpCode.FRIEND_GETSLOTFROMNAME;
import static com.jagex.core.constants.ClientScriptOpCode.FRIEND_GETWORLD;
import static com.jagex.core.constants.ClientScriptOpCode.FRIEND_GETWORLDNAME;
import static com.jagex.core.constants.ClientScriptOpCode.FRIEND_IS_REFERRED;
import static com.jagex.core.constants.ClientScriptOpCode.FRIEND_SAME_GAME;
import static com.jagex.core.constants.ClientScriptOpCode.FRIEND_SETRANK;
import static com.jagex.core.constants.ClientScriptOpCode.FRIEND_TEST;
import static com.jagex.core.constants.ClientScriptOpCode.FROMBILLING;
import static com.jagex.core.constants.ClientScriptOpCode.FROMDATE;
import static com.jagex.core.constants.ClientScriptOpCode.FULLSCREEN_ENTER;
import static com.jagex.core.constants.ClientScriptOpCode.FULLSCREEN_EXIT;
import static com.jagex.core.constants.ClientScriptOpCode.FULLSCREEN_GETMODE;
import static com.jagex.core.constants.ClientScriptOpCode.FULLSCREEN_LASTMODE;
import static com.jagex.core.constants.ClientScriptOpCode.FULLSCREEN_MODECOUNT;
import static com.jagex.core.constants.ClientScriptOpCode.GENDER;
import static com.jagex.core.constants.ClientScriptOpCode.GETCLIPBOARD;
import static com.jagex.core.constants.ClientScriptOpCode.GETDEFAULTWINDOWMODE;
import static com.jagex.core.constants.ClientScriptOpCode.GETWINDOWMODE;
import static com.jagex.core.constants.ClientScriptOpCode.GET_ACTIVE_MINIMENU_ENTRY;
import static com.jagex.core.constants.ClientScriptOpCode.GET_CURRENTCURSOR;
import static com.jagex.core.constants.ClientScriptOpCode.GET_MINIMENU_LENGTH;
import static com.jagex.core.constants.ClientScriptOpCode.GET_MOUSEBUTTONS;
import static com.jagex.core.constants.ClientScriptOpCode.GET_MOUSEX;
import static com.jagex.core.constants.ClientScriptOpCode.GET_MOUSEY;
import static com.jagex.core.constants.ClientScriptOpCode.GET_SECOND_MINIMENU_ENTRY;
import static com.jagex.core.constants.ClientScriptOpCode.GET_SELFYANGLE;
import static com.jagex.core.constants.ClientScriptOpCode.GOSUB_WITH_PARAMS;
import static com.jagex.core.constants.ClientScriptOpCode.HSVTORGB;
import static com.jagex.core.constants.ClientScriptOpCode.IF_CLOSE;
import static com.jagex.core.constants.ClientScriptOpCode.IF_CLOSESUBCLIENT;
import static com.jagex.core.constants.ClientScriptOpCode.IF_DEBUG_BUTTON1;
import static com.jagex.core.constants.ClientScriptOpCode.IF_DEBUG_BUTTON10;
import static com.jagex.core.constants.ClientScriptOpCode.IF_DEBUG_BUTTON2;
import static com.jagex.core.constants.ClientScriptOpCode.IF_DEBUG_BUTTON3;
import static com.jagex.core.constants.ClientScriptOpCode.IF_DEBUG_BUTTON4;
import static com.jagex.core.constants.ClientScriptOpCode.IF_DEBUG_BUTTON5;
import static com.jagex.core.constants.ClientScriptOpCode.IF_DEBUG_BUTTON6;
import static com.jagex.core.constants.ClientScriptOpCode.IF_DEBUG_BUTTON7;
import static com.jagex.core.constants.ClientScriptOpCode.IF_DEBUG_BUTTON8;
import static com.jagex.core.constants.ClientScriptOpCode.IF_DEBUG_BUTTON9;
import static com.jagex.core.constants.ClientScriptOpCode.IF_DEBUG_GETCOMCOUNT;
import static com.jagex.core.constants.ClientScriptOpCode.IF_DEBUG_GETCOMNAME;
import static com.jagex.core.constants.ClientScriptOpCode.IF_DEBUG_GETNAME;
import static com.jagex.core.constants.ClientScriptOpCode.IF_DEBUG_GETOPENIFCOUNT;
import static com.jagex.core.constants.ClientScriptOpCode.IF_DEBUG_GETSERVERTRIGGERS;
import static com.jagex.core.constants.ClientScriptOpCode.IF_DEBUG_TARGET;
import static com.jagex.core.constants.ClientScriptOpCode.IF_DRAGPICKUP;
import static com.jagex.core.constants.ClientScriptOpCode.IF_FIND;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GET2DANGLE;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETCOLOUR;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETFONTGRAPHIC;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETGRAPHIC;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETHEIGHT;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETHIDE;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETINVCOUNT;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETINVOBJECT;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETLAYER;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETMODEL;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETMODELANGLE_X;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETMODELANGLE_Y;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETMODELANGLE_Z;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETMODELXOF;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETMODELYOF;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETMODELZOOM;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETNEXTSUBID;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETOP;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETOPBASE;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETPARENTLAYER;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETSCROLLHEIGHT;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETSCROLLWIDTH;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETSCROLLX;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETSCROLLY;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETTARGETMASK;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETTEXT;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETTOP;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETTRANS;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETWIDTH;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETX;
import static com.jagex.core.constants.ClientScriptOpCode.IF_GETY;
import static com.jagex.core.constants.ClientScriptOpCode.IF_HASSUB;
import static com.jagex.core.constants.ClientScriptOpCode.IF_HASSUBMODAL;
import static com.jagex.core.constants.ClientScriptOpCode.IF_HASSUBOVERLAY;
import static com.jagex.core.constants.ClientScriptOpCode.IF_OPENSUBCLIENT;
import static com.jagex.core.constants.ClientScriptOpCode.IF_SENDTOBACK;
import static com.jagex.core.constants.ClientScriptOpCode.IF_SENDTOFRONT;
import static com.jagex.core.constants.ClientScriptOpCode.IGNORE_ADD;
import static com.jagex.core.constants.ClientScriptOpCode.IGNORE_ADD_TEMP;
import static com.jagex.core.constants.ClientScriptOpCode.IGNORE_COUNT;
import static com.jagex.core.constants.ClientScriptOpCode.IGNORE_DEL;
import static com.jagex.core.constants.ClientScriptOpCode.IGNORE_GETNAME;
import static com.jagex.core.constants.ClientScriptOpCode.IGNORE_GETNAME_UNFILTERED;
import static com.jagex.core.constants.ClientScriptOpCode.IGNORE_IS_TEMP;
import static com.jagex.core.constants.ClientScriptOpCode.IGNORE_TEXT;
import static com.jagex.core.constants.ClientScriptOpCode.INTERPOLATE;
import static com.jagex.core.constants.ClientScriptOpCode.INVOTHER_GETNUM;
import static com.jagex.core.constants.ClientScriptOpCode.INVOTHER_GETOBJ;
import static com.jagex.core.constants.ClientScriptOpCode.INVOTHER_TOTAL;
import static com.jagex.core.constants.ClientScriptOpCode.INVPOW;
import static com.jagex.core.constants.ClientScriptOpCode.INV_FREESPACE;
import static com.jagex.core.constants.ClientScriptOpCode.INV_GETNUM;
import static com.jagex.core.constants.ClientScriptOpCode.INV_GETOBJ;
import static com.jagex.core.constants.ClientScriptOpCode.INV_SIZE;
import static com.jagex.core.constants.ClientScriptOpCode.INV_TOTAL;
import static com.jagex.core.constants.ClientScriptOpCode.INV_TOTALPARAM;
import static com.jagex.core.constants.ClientScriptOpCode.INV_TOTALPARAM_STACK;
import static com.jagex.core.constants.ClientScriptOpCode.JOIN_STRING;
import static com.jagex.core.constants.ClientScriptOpCode.KEYHELD_ALT;
import static com.jagex.core.constants.ClientScriptOpCode.KEYHELD_CTRL;
import static com.jagex.core.constants.ClientScriptOpCode.KEYHELD_SHIFT;
import static com.jagex.core.constants.ClientScriptOpCode.LASTLOGIN;
import static com.jagex.core.constants.ClientScriptOpCode.LC_PARAM;
import static com.jagex.core.constants.ClientScriptOpCode.LOBBY_ENTERGAME;
import static com.jagex.core.constants.ClientScriptOpCode.LOBBY_ENTERGAME_REPLY;
import static com.jagex.core.constants.ClientScriptOpCode.LOBBY_ENTERLOBBY;
import static com.jagex.core.constants.ClientScriptOpCode.LOBBY_ENTERLOBBYREPLY;
import static com.jagex.core.constants.ClientScriptOpCode.LOBBY_ENTERLOBBY_SOCIAL_NETWORK;
import static com.jagex.core.constants.ClientScriptOpCode.LOBBY_LEAVELOBBY;
import static com.jagex.core.constants.ClientScriptOpCode.LOGIN_CANCEL;
import static com.jagex.core.constants.ClientScriptOpCode.LOGIN_CONTINUE;
import static com.jagex.core.constants.ClientScriptOpCode.LOGIN_DISALLOWETRIGGER;
import static com.jagex.core.constants.ClientScriptOpCode.LOGIN_DISALLOWRESULT;
import static com.jagex.core.constants.ClientScriptOpCode.LOGIN_HOPTIME;
import static com.jagex.core.constants.ClientScriptOpCode.LOGIN_INPROGRESS;
import static com.jagex.core.constants.ClientScriptOpCode.LOGIN_LAST_TRANSFER_REPLY;
import static com.jagex.core.constants.ClientScriptOpCode.LOGIN_QUEUE_POSITION;
import static com.jagex.core.constants.ClientScriptOpCode.LOGIN_REPLY;
import static com.jagex.core.constants.ClientScriptOpCode.LOGIN_REQUEST;
import static com.jagex.core.constants.ClientScriptOpCode.LOGIN_REQUEST_SOCIAL_NETWORK;
import static com.jagex.core.constants.ClientScriptOpCode.LOGIN_RESETREPLY;
import static com.jagex.core.constants.ClientScriptOpCode.LONG_BRANCH_EQUALS;
import static com.jagex.core.constants.ClientScriptOpCode.LONG_BRANCH_GREATER_THAN;
import static com.jagex.core.constants.ClientScriptOpCode.LONG_BRANCH_GREATER_THAN_OR_EQUALS;
import static com.jagex.core.constants.ClientScriptOpCode.LONG_BRANCH_LESS_THAN;
import static com.jagex.core.constants.ClientScriptOpCode.LONG_BRANCH_LESS_THAN_OR_EQUALS;
import static com.jagex.core.constants.ClientScriptOpCode.LONG_BRANCH_NOT;
import static com.jagex.core.constants.ClientScriptOpCode.LOWERCASE;
import static com.jagex.core.constants.ClientScriptOpCode.MAP_ISOWNER;
import static com.jagex.core.constants.ClientScriptOpCode.MAP_LANG;
import static com.jagex.core.constants.ClientScriptOpCode.MAP_MEMBERS;
import static com.jagex.core.constants.ClientScriptOpCode.MAP_QUICKCHAT;
import static com.jagex.core.constants.ClientScriptOpCode.MAP_WORLD;
import static com.jagex.core.constants.ClientScriptOpCode.MAX;
import static com.jagex.core.constants.ClientScriptOpCode.MEC_CATEGORY;
import static com.jagex.core.constants.ClientScriptOpCode.MEC_PARAM;
import static com.jagex.core.constants.ClientScriptOpCode.MEC_SPRITE;
import static com.jagex.core.constants.ClientScriptOpCode.MEC_TEXT;
import static com.jagex.core.constants.ClientScriptOpCode.MEC_TEXTSIZE;
import static com.jagex.core.constants.ClientScriptOpCode.MES;
import static com.jagex.core.constants.ClientScriptOpCode.MES_TYPED;
import static com.jagex.core.constants.ClientScriptOpCode.MIN;
import static com.jagex.core.constants.ClientScriptOpCode.MINIMENUOPEN;
import static com.jagex.core.constants.ClientScriptOpCode.MODULO;
import static com.jagex.core.constants.ClientScriptOpCode.MOVE_COORD;
import static com.jagex.core.constants.ClientScriptOpCode.MULTIPLY;
import static com.jagex.core.constants.ClientScriptOpCode.NC_PARAM;
import static com.jagex.core.constants.ClientScriptOpCode.NOTIFY_ACCOUNTCREATED;
import static com.jagex.core.constants.ClientScriptOpCode.NOTIFY_ACCOUNTCREATESTARTED;
import static com.jagex.core.constants.ClientScriptOpCode.OC_CERT;
import static com.jagex.core.constants.ClientScriptOpCode.OC_COST;
import static com.jagex.core.constants.ClientScriptOpCode.OC_FIND;
import static com.jagex.core.constants.ClientScriptOpCode.OC_FINDNEXT;
import static com.jagex.core.constants.ClientScriptOpCode.OC_FINDRESTART;
import static com.jagex.core.constants.ClientScriptOpCode.OC_ICURSOR;
import static com.jagex.core.constants.ClientScriptOpCode.OC_IOP;
import static com.jagex.core.constants.ClientScriptOpCode.OC_MEMBERS;
import static com.jagex.core.constants.ClientScriptOpCode.OC_MULTISTACKSIZE;
import static com.jagex.core.constants.ClientScriptOpCode.OC_NAME;
import static com.jagex.core.constants.ClientScriptOpCode.OC_OP;
import static com.jagex.core.constants.ClientScriptOpCode.OC_PARAM;
import static com.jagex.core.constants.ClientScriptOpCode.OC_STACKABLE;
import static com.jagex.core.constants.ClientScriptOpCode.OC_UNCERT;
import static com.jagex.core.constants.ClientScriptOpCode.OPENURL;
import static com.jagex.core.constants.ClientScriptOpCode.OPENURL_NOLOGIN;
import static com.jagex.core.constants.ClientScriptOpCode.OPPLAYER;
import static com.jagex.core.constants.ClientScriptOpCode.OPPLAYERT;
import static com.jagex.core.constants.ClientScriptOpCode.OR;
import static com.jagex.core.constants.ClientScriptOpCode.ORLDMAP_GETMAPNAME;
import static com.jagex.core.constants.ClientScriptOpCode.PARAHEIGHT;
import static com.jagex.core.constants.ClientScriptOpCode.PARAWIDTH;
import static com.jagex.core.constants.ClientScriptOpCode.PLAYERCOUNTRY;
import static com.jagex.core.constants.ClientScriptOpCode.PLAYERDEMO;
import static com.jagex.core.constants.ClientScriptOpCode.PLAYERMEMBER;
import static com.jagex.core.constants.ClientScriptOpCode.PLAYERMOD;
import static com.jagex.core.constants.ClientScriptOpCode.PLAYERMODLEVEL;
import static com.jagex.core.constants.ClientScriptOpCode.POP_ARRAY_INT;
import static com.jagex.core.constants.ClientScriptOpCode.POP_INT_DISCARD;
import static com.jagex.core.constants.ClientScriptOpCode.POP_INT_LOCAL;
import static com.jagex.core.constants.ClientScriptOpCode.POP_LONG_DISCARD;
import static com.jagex.core.constants.ClientScriptOpCode.POP_LONG_LOCAL;
import static com.jagex.core.constants.ClientScriptOpCode.POP_STRING_DISCARD;
import static com.jagex.core.constants.ClientScriptOpCode.POP_STRING_LOCAL;
import static com.jagex.core.constants.ClientScriptOpCode.POP_VAR;
import static com.jagex.core.constants.ClientScriptOpCode.POP_VARBIT;
import static com.jagex.core.constants.ClientScriptOpCode.POP_VARC;
import static com.jagex.core.constants.ClientScriptOpCode.POP_VARCSTR;
import static com.jagex.core.constants.ClientScriptOpCode.POW;
import static com.jagex.core.constants.ClientScriptOpCode.PROFILE_CPU;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_ARRAY_INT;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_CONSTANT_INT;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_CONSTANT_STRING;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_INT_LOCAL;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_LONG_CONSTANT;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_LONG_LOCAL;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_STRING_LOCAL;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_VAR;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_VARBIT;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_VARC;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_VARCLAN;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_VARCLANBIT;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_VARCLANSETTING;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_VARCLANSETTINGBIT;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_VARCLANSETTING_STRING;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_VARCLAN_LONG;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_VARCLAN_SETTING_LONG;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_VARCLAN_STRING;
import static com.jagex.core.constants.ClientScriptOpCode.PUSH_VARCSTR;
import static com.jagex.core.constants.ClientScriptOpCode.QUIT;
import static com.jagex.core.constants.ClientScriptOpCode.RANDOM;
import static com.jagex.core.constants.ClientScriptOpCode.RANDOMINC;
import static com.jagex.core.constants.ClientScriptOpCode.RANDOM_SOUND_PITCH;
import static com.jagex.core.constants.ClientScriptOpCode.REBOOTTIMER;
import static com.jagex.core.constants.ClientScriptOpCode.REMOVETAGS;
import static com.jagex.core.constants.ClientScriptOpCode.RESUME_CLANFORUMQFCDIALOG;
import static com.jagex.core.constants.ClientScriptOpCode.RESUME_COUNTDIALOG;
import static com.jagex.core.constants.ClientScriptOpCode.RESUME_HSLDIALOG;
import static com.jagex.core.constants.ClientScriptOpCode.RESUME_NAMEDIALOG;
import static com.jagex.core.constants.ClientScriptOpCode.RESUME_OBJDIALOG;
import static com.jagex.core.constants.ClientScriptOpCode.RESUME_STRINGDIALOG;
import static com.jagex.core.constants.ClientScriptOpCode.RETURN;
import static com.jagex.core.constants.ClientScriptOpCode.RUNENERGY_VISIBLE;
import static com.jagex.core.constants.ClientScriptOpCode.RUNWEIGHT_VISIBLE;
import static com.jagex.core.constants.ClientScriptOpCode.SCALE;
import static com.jagex.core.constants.ClientScriptOpCode.SETBIT;
import static com.jagex.core.constants.ClientScriptOpCode.SETCLIENTPALETTE;
import static com.jagex.core.constants.ClientScriptOpCode.SETDEFAULTCURSORS;
import static com.jagex.core.constants.ClientScriptOpCode.SETDEFAULTWINDOWMODE;
import static com.jagex.core.constants.ClientScriptOpCode.SETGENDER;
import static com.jagex.core.constants.ClientScriptOpCode.SETHARDCODEDOPCURSORS;
import static com.jagex.core.constants.ClientScriptOpCode.SETOBJ;
import static com.jagex.core.constants.ClientScriptOpCode.SETSUBMENUMINLENGTH;
import static com.jagex.core.constants.ClientScriptOpCode.SETUP_MESSAGEBOX;
import static com.jagex.core.constants.ClientScriptOpCode.SETWINDOWMODE;
import static com.jagex.core.constants.ClientScriptOpCode.SOUND_JINGLE;
import static com.jagex.core.constants.ClientScriptOpCode.SOUND_JINGLE_VOLUME;
import static com.jagex.core.constants.ClientScriptOpCode.SOUND_SONG;
import static com.jagex.core.constants.ClientScriptOpCode.SOUND_SONG_VOLUME;
import static com.jagex.core.constants.ClientScriptOpCode.SOUND_SPEECH_VOLUME;
import static com.jagex.core.constants.ClientScriptOpCode.SOUND_SYNTH;
import static com.jagex.core.constants.ClientScriptOpCode.SOUND_SYNTH_RATE;
import static com.jagex.core.constants.ClientScriptOpCode.SOUND_SYTHN_VOLUME;
import static com.jagex.core.constants.ClientScriptOpCode.SOUND_VORBIS_RATE;
import static com.jagex.core.constants.ClientScriptOpCode.SOUND_VORBIS_VOLUME;
import static com.jagex.core.constants.ClientScriptOpCode.SPLINE_ADDPOINT;
import static com.jagex.core.constants.ClientScriptOpCode.SPLINE_LENGTH;
import static com.jagex.core.constants.ClientScriptOpCode.SPLINE_NEW;
import static com.jagex.core.constants.ClientScriptOpCode.STAFFMODLEVEL;
import static com.jagex.core.constants.ClientScriptOpCode.STAT;
import static com.jagex.core.constants.ClientScriptOpCode.STAT_BASE;
import static com.jagex.core.constants.ClientScriptOpCode.STAT_VISIBLE_XP;
import static com.jagex.core.constants.ClientScriptOpCode.STOCKMARKET_GETOFFERCOMPLETEDCOUNT;
import static com.jagex.core.constants.ClientScriptOpCode.STOCKMARKET_GETOFFERCOMPLETEDGOLD;
import static com.jagex.core.constants.ClientScriptOpCode.STOCKMARKET_GETOFFERCOUNT;
import static com.jagex.core.constants.ClientScriptOpCode.STOCKMARKET_GETOFFERITEM;
import static com.jagex.core.constants.ClientScriptOpCode.STOCKMARKET_GETOFFERPRICE;
import static com.jagex.core.constants.ClientScriptOpCode.STOCKMARKET_GETOFFERTYPE;
import static com.jagex.core.constants.ClientScriptOpCode.STOCKMARKET_ISOFFERADDING;
import static com.jagex.core.constants.ClientScriptOpCode.STOCKMARKET_ISOFFEREMPTY;
import static com.jagex.core.constants.ClientScriptOpCode.STOCKMARKET_ISOFFERFINISHED;
import static com.jagex.core.constants.ClientScriptOpCode.STOCKMARKET_ISOFFERSTABLE;
import static com.jagex.core.constants.ClientScriptOpCode.STRINGWIDTH;
import static com.jagex.core.constants.ClientScriptOpCode.STRING_INDEXOF_CHAR;
import static com.jagex.core.constants.ClientScriptOpCode.STRING_INDEXOF_STRING;
import static com.jagex.core.constants.ClientScriptOpCode.STRING_LENGTH;
import static com.jagex.core.constants.ClientScriptOpCode.STRUCT_PARAM;
import static com.jagex.core.constants.ClientScriptOpCode.SUB;
import static com.jagex.core.constants.ClientScriptOpCode.SUBSTRING;
import static com.jagex.core.constants.ClientScriptOpCode.SWITCH;
import static com.jagex.core.constants.ClientScriptOpCode.TESTBIT;
import static com.jagex.core.constants.ClientScriptOpCode.TEXT_GENDER;
import static com.jagex.core.constants.ClientScriptOpCode.TEXT_SWITCH;
import static com.jagex.core.constants.ClientScriptOpCode.TOSTRING;
import static com.jagex.core.constants.ClientScriptOpCode.TOSTRING_LOCALISED;
import static com.jagex.core.constants.ClientScriptOpCode.UDETAIL_DOB;
import static com.jagex.core.constants.ClientScriptOpCode.USERDETAIL_LOBBY_CCEXPIRY;
import static com.jagex.core.constants.ClientScriptOpCode.USERDETAIL_LOBBY_DOBREQUESTED;
import static com.jagex.core.constants.ClientScriptOpCode.USERDETAIL_LOBBY_EMAILSTATUS;
import static com.jagex.core.constants.ClientScriptOpCode.USERDETAIL_LOBBY_GRACEEXPIRY;
import static com.jagex.core.constants.ClientScriptOpCode.USERDETAIL_LOBBY_JCOINS_BALANCE;
import static com.jagex.core.constants.ClientScriptOpCode.USERDETAIL_LOBBY_LASTLOGINADDRESS;
import static com.jagex.core.constants.ClientScriptOpCode.USERDETAIL_LOBBY_LASTLOGINDAY;
import static com.jagex.core.constants.ClientScriptOpCode.USERDETAIL_LOBBY_LOYALTY;
import static com.jagex.core.constants.ClientScriptOpCode.USERDETAIL_LOBBY_LOYALTYBALANCE;
import static com.jagex.core.constants.ClientScriptOpCode.USERDETAIL_LOBBY_MEMBERSHIP;
import static com.jagex.core.constants.ClientScriptOpCode.USERDETAIL_LOBBY_MEMBERSSTATS;
import static com.jagex.core.constants.ClientScriptOpCode.USERDETAIL_LOBBY_PLAYAGE;
import static com.jagex.core.constants.ClientScriptOpCode.USERDETAIL_LOBBY_RECOVERYDAY;
import static com.jagex.core.constants.ClientScriptOpCode.USERDETAIL_LOBBY_UNREADMESSAGES;
import static com.jagex.core.constants.ClientScriptOpCode.USERDETAIL_QUICKCHAT;
import static com.jagex.core.constants.ClientScriptOpCode.USERFLOW_FLAGS;
import static com.jagex.core.constants.ClientScriptOpCode.VIDEO_ADVERT_ALLOW_SKIP;
import static com.jagex.core.constants.ClientScriptOpCode.VIDEO_ADVERT_FORCE_REMOVE;
import static com.jagex.core.constants.ClientScriptOpCode.VIDEO_ADVERT_PLAY;
import static com.jagex.core.constants.ClientScriptOpCode.VIEWPORT_CLAMPFOV;
import static com.jagex.core.constants.ClientScriptOpCode.VIEWPORT_GETEFFECTIVESIZE;
import static com.jagex.core.constants.ClientScriptOpCode.VIEWPORT_GETFOV;
import static com.jagex.core.constants.ClientScriptOpCode.VIEWPORT_GETZOOM;
import static com.jagex.core.constants.ClientScriptOpCode.VIEWPORT_SETFOV;
import static com.jagex.core.constants.ClientScriptOpCode.VIEWPORT_SETZOOM;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDLIST_AUTOWORLD;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDLIST_FETCH;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDLIST_NEXT;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDLIST_PINGWORLDS;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDLIST_SORT;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDLIST_SPECIFIC;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDLIST_SPECIFIC_THISWORLD;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDLIST_START;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDLIST_SWITCH;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_CLOSEMAP;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_COORDINMAP;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_DISABLEELEMENT;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_DISABLEELEMENTCATEGORY;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_DISABLEELEMENTS;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_FINDNEARESTELEMENT;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_FLASHELEMENT;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_FLASHELEMENTCATEGORY;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_GETCONFIGBOUNDS;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_GETCONFIGORIGIN;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_GETCONFIGZOOM;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_GETCURRENTMAP;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_GETDISABLEELEMENT;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_GETDISABLEELEMENTCATEGORY;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_GETDISABLEELEMENTS;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_GETDISPLAYCOORD;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_GETDISPLAYPOSITION;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_GETMAP;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_GETSIZE;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_GETSOURCECOORD;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_GETSOURCEPOSITION;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_GETZOOM;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_ISLOADED;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_JUMPTODISPLAYCOORD;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_JUMPTOSOURCECOORD;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_LISTELEMENT_NEXT;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_LISTELEMENT_START;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_SETMAP;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_SETMAP_COORD;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_SETMAP_COORD_OVERRIDE;
import static com.jagex.core.constants.ClientScriptOpCode.WORLDMAP_SETZOOM;
import static com.jagex.core.constants.ClientScriptOpCode.WRITECONSOLE;

@OriginalClass("client!ou")
public final class ScriptRunner {

    @OriginalMember(owner = "client!ou", name = "y", descriptor = "[Ljava/lang/String;")
    public static String[] stringVars;

    @OriginalMember(owner = "client!ou", name = "v", descriptor = "[I")
    public static int[] intVars;

    @OriginalMember(owner = "client!ou", name = "m", descriptor = "Lclient!hda;")
    public static Component activeComponent;

    @OriginalMember(owner = "client!ou", name = "i", descriptor = "[J")
    public static long[] longVars;

    @OriginalMember(owner = "client!ou", name = "H", descriptor = "Lclient!an;")
    public static QuickChatPhrase activeChatPhrase;

    @OriginalMember(owner = "client!ou", name = "z", descriptor = "Lclient!rfa;")
    public static ClanChannel activeClanChannel;

    @OriginalMember(owner = "client!ou", name = "G", descriptor = "Lclient!hi;")
    public static ClanSettings activeClanSettings;

    @OriginalMember(owner = "client!ou", name = "k", descriptor = "Lclient!hda;")
    public static Component activeComponent2;

    @OriginalMember(owner = "client!ou", name = "B", descriptor = "[J")
    public static final long[] longStack = new long[1000];

    @OriginalMember(owner = "client!ou", name = "e", descriptor = "[I")
    public static final int[] intStack = new int[1000];

    @OriginalMember(owner = "client!ou", name = "D", descriptor = "I")
    public static int stringStackPointer = 0;

    @OriginalMember(owner = "client!ou", name = "t", descriptor = "[Lclient!gf;")
    public static final StackFrame[] frames = new StackFrame[50];

    @OriginalMember(owner = "client!ou", name = "r", descriptor = "I")
    public static int framePointer = 0;

    @OriginalMember(owner = "client!ou", name = "f", descriptor = "[I")
    public static final int[] areaCoord = new int[3];

    @OriginalMember(owner = "client!ou", name = "n", descriptor = "I")
    public static int intStackPointer = 0;

    @OriginalMember(owner = "client!ou", name = "b", descriptor = "[Ljava/lang/String;")
    public static final String[] stringStack = new String[1000];

    @OriginalMember(owner = "client!ou", name = "w", descriptor = "[[I")
    public static final int[][] arrayVars = new int[5][5000];

    @OriginalMember(owner = "client!ou", name = "C", descriptor = "I")
    public static int longStackPointer = 0;

    @OriginalMember(owner = "client!ou", name = "g", descriptor = "[I")
    public static final int[] arrayLengths = new int[5];

    @OriginalMember(owner = "client!ou", name = "E", descriptor = "Lclient!dla;")
    public static final ReferenceCache A_WEIGHTED_CACHE___156 = new ReferenceCache(4);

    @OriginalMember(owner = "client!ou", name = "q", descriptor = "Z")
    public static boolean debug = false;

    @OriginalMember(owner = "client!ou", name = "o", descriptor = "I")
    public static int lastHookId = 0;

    @OriginalMember(owner = "client!ou", name = "s", descriptor = "Ljava/lang/String;")
    public static String debugName = null;

    @OriginalMember(owner = "client!ou", name = "b", descriptor = "(I)I")
    public static int getClanSettingVarbit(@OriginalArg(0) int id) {
        @Pc(4) VarClanType type = VarClanTypeList.instance.list(id);
        if (type == null) {
            throw new RuntimeException("sr-c113");
        }

        @Pc(29) Integer setting = activeClanSettings.getExtraSettingVarbit((Client.modeGame.id << 16) | type.baseVar, type.endBit, type.startBit);
        if (setting == null) {
            return 0;
        } else {
            return setting;
        }
    }

    @OriginalMember(owner = "client!ou", name = "a", descriptor = "(I)I")
    public static int getClanSettingInt(@OriginalArg(0) int id) {
        @Pc(4) VarClanType type = VarClanTypeList.instance.list(id);
        if (type == null) {
            throw new RuntimeException("sr-c112");
        }

        @Pc(24) Integer setting = activeClanSettings.getExtraSettingInt((Client.modeGame.id << 16) | id);
        if (setting == null) {
            return (type.dataType == 'i' || type.dataType == '1') ? 0 : -1;
        } else {
            return setting;
        }
    }

    @OriginalMember(owner = "client!ou", name = "c", descriptor = "(I)J")
    public static long getClanSettingLong(@OriginalArg(0) int id) {
        @Pc(9) Long setting = activeClanSettings.getExtraSettingLong(Client.modeGame.id << 16 | id);
        if (setting == null) {
            return -1L;
        } else {
            return setting;
        }
    }

    @OriginalMember(owner = "client!ou", name = "d", descriptor = "(I)V")
    public static void executeOnLoad(@OriginalArg(0) int id) {
        if (id == -1 || !InterfaceList.load(id)) {
            return;
        }

        @Pc(14) Component[] components = InterfaceList.interfaces[id];
        for (@Pc(16) int i = 0; i < components.length; i++) {
            @Pc(21) Component component = components[i];

            if (component.onLoad != null) {
                @Pc(28) HookRequest hook = new HookRequest();
                hook.source = component;
                hook.arguments = component.onLoad;
                executeHookInner(hook, 2000000);
            }
        }
    }

    @OriginalMember(owner = "client!ou", name = "b", descriptor = "(Lclient!hda;)V")
    public static void sendToFront(@OriginalArg(0) Component component) {
        if (component == null) {
            return;
        }

        if (component.id != -1) {
            @Pc(12) Component layer = InterfaceList.list(component.layer);
            if (layer == null) {
                return;
            }

            if (layer.dynamicComponents == layer.staticComponents) {
                layer.dynamicComponents = new Component[layer.staticComponents.length];
                layer.dynamicComponents[layer.dynamicComponents.length - 1] = component;
                Arrays.copy(layer.staticComponents, 0, layer.dynamicComponents, 0, component.id);
                Arrays.copy(layer.staticComponents, component.id + 1, layer.dynamicComponents, component.id, layer.staticComponents.length - component.id - 1);
                return;
            }

            @Pc(68) int i = 0;
            @Pc(71) Component[] dynamicComponents = layer.dynamicComponents;
            while (i < dynamicComponents.length && component != dynamicComponents[i]) {
                i++;
            }
            if (i >= dynamicComponents.length) {
                return;
            }

            Arrays.copy(dynamicComponents, i + 1, dynamicComponents, i, dynamicComponents.length - i - 1);
            dynamicComponents[layer.dynamicComponents.length - 1] = component;
        } else {
            @Pc(119) int parent = component.slot >>> 16;
            @Pc(123) Component[] children = InterfaceList.cache[parent];

            if (children == null) {
                @Pc(71) Component[] newChildren = InterfaceList.interfaces[parent];
                @Pc(132) int length = newChildren.length;
                children = InterfaceList.cache[parent] = new Component[length];
                Arrays.copy(newChildren, 0, children, 0, newChildren.length);
            }

            @Pc(148) int i;
            for (i = 0; i < children.length; i++) {
                if (component == children[i]) {
                    break;
                }
            }
            if (i >= children.length) {
                return;
            }

            Arrays.copy(children, i + 1, children, i, children.length - i - 1);
            children[children.length - 1] = component;
        }
    }

    @OriginalMember(owner = "client!ou", name = "a", descriptor = "(IZ)V")
    public static void handleSmallOp(@OriginalArg(0) int op, @OriginalArg(1) boolean secondary) {
        if (op < 300) {
            if (op == CC_CREATE) {
                intStackPointer -= 3;

                @Pc(15) int parentId = intStack[intStackPointer];
                @Pc(21) int componentType = intStack[intStackPointer + 1];
                @Pc(27) int componentId = intStack[intStackPointer + 2];

                if (componentType == Component.TYPE_LAYER) {
                    throw new RuntimeException();
                }

                @Pc(38) Component parent = InterfaceList.list(parentId);
                if (parent.staticComponents == null) {
                    parent.staticComponents = new Component[componentId + 1];
                    parent.dynamicComponents = parent.staticComponents;
                }

                if (parent.staticComponents.length <= componentId) {
                    if (parent.staticComponents == parent.dynamicComponents) {
                        @Pc(70) Component[] staticComponents = new Component[componentId + 1];
                        for (@Pc(72) int i = 0; i < parent.staticComponents.length; i++) {
                            staticComponents[i] = parent.staticComponents[i];
                        }
                        parent.staticComponents = parent.dynamicComponents = staticComponents;
                    } else {
                        @Pc(70) Component[] staticComponents = new Component[componentId + 1];
                        @Pc(104) Component[] dynamicComponents = new Component[componentId + 1];
                        for (@Pc(106) int i = 0; i < parent.staticComponents.length; i++) {
                            staticComponents[i] = parent.staticComponents[i];
                            dynamicComponents[i] = parent.dynamicComponents[i];
                        }
                        parent.staticComponents = staticComponents;
                        parent.dynamicComponents = dynamicComponents;
                    }
                }

                if (componentId > 0 && parent.staticComponents[componentId - 1] == null) {
                    throw new RuntimeException("Gap at:" + (componentId - 1));
                }

                @Pc(166) Component cc = new Component();
                cc.type = componentType;
                cc.layer = cc.slot = parent.slot;
                cc.id = componentId;

                parent.staticComponents[componentId] = cc;

                if (parent.dynamicComponents != parent.staticComponents) {
                    parent.dynamicComponents[componentId] = cc;
                }

                if (secondary) {
                    activeComponent2 = cc;
                } else {
                    activeComponent = cc;
                }

                InterfaceManager.redraw(parent);
                return;
            }

            if (op == CC_DELETE) {
                @Pc(220) Component cc = secondary ? activeComponent2 : activeComponent;
                if (cc.id == -1) {
                    if (secondary) {
                        throw new RuntimeException("Tried to .cc_delete static .active-component!");
                    }

                    throw new RuntimeException("Tried to cc_delete static active-component!");
                }

                @Pc(248) Component component = InterfaceList.list(cc.slot);
                component.staticComponents[cc.id] = null;
                InterfaceManager.redraw(component);
                return;
            }

            if (op == CC_DELETEALL) {
                @Pc(220) Component cc = InterfaceList.list(intStack[--intStackPointer]);
                cc.staticComponents = null;
                cc.dynamicComponents = null;
                InterfaceManager.redraw(cc);
                return;
            }

            if (op == CC_FIND) {
                intStackPointer -= 2;
                @Pc(15) int idAndSlot = intStack[intStackPointer];
                @Pc(21) int component = intStack[intStackPointer + 1];
                @Pc(303) Component cc = InterfaceList.getComponent(idAndSlot, component);

                if (cc != null && component != -1) {
                    intStack[intStackPointer++] = 1;

                    if (secondary) {
                        activeComponent2 = cc;
                        return;
                    } else {
                        activeComponent = cc;
                        return;
                    }
                }

                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == IF_FIND) {
                @Pc(15) int idAndSlot = intStack[--intStackPointer];
                @Pc(248) Component component = InterfaceList.list(idAndSlot);

                if (component != null) {
                    intStack[intStackPointer++] = 1;

                    if (secondary) {
                        activeComponent2 = component;
                        return;
                    }

                    activeComponent = component;
                    return;
                }

                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == IF_SENDTOFRONT || op == CC_SENDTOFRONT) {
                @Pc(220) Component component;

                if (op == IF_SENDTOFRONT) {
                    @Pc(21) int idAndSlot = intStack[--intStackPointer];
                    component = InterfaceList.list(idAndSlot);
                } else {
                    component = secondary ? activeComponent2 : activeComponent;
                }

                sendToFront(component);
                return;
            }

            if (op == IF_SENDTOBACK || op == CC_SENDTOBACK) {
                @Pc(220) Component component;

                if (op == IF_SENDTOBACK) {
                    @Pc(21) int idAndSlot = intStack[--intStackPointer];
                    component = InterfaceList.list(idAndSlot);
                } else {
                    component = secondary ? activeComponent2 : activeComponent;
                }

                sendToBack(component);
                return;
            }
        } else if (op < 500) {
            if (op == BASEIDKIT) {
                intStackPointer -= 2;

                @Pc(15) int value = intStack[intStackPointer];
                @Pc(21) int idkType = intStack[intStackPointer + 1];

                if (PlayerEntity.self.playerModel == null) {
                    return;
                }

                for (@Pc(27) int part = 0; part < PlayerModel.PRIMARY_BODY_PARTS.length; part++) {
                    if (PlayerModel.PRIMARY_BODY_PARTS[part] == value) {
                        PlayerEntity.self.playerModel.setIDKPart(idkType, IDKTypeList.instance, part);
                        return;
                    }
                }

                for (@Pc(506) int part = 0; part < PlayerModel.SECONDARY_BODY_PARTS.length; part++) {
                    if (PlayerModel.SECONDARY_BODY_PARTS[part] == value) {
                        PlayerEntity.self.playerModel.setIDKPart(idkType, IDKTypeList.instance, part);
                        return;
                    }
                }

                return;
            }

            if (op == BASECOLOUR) {
                intStackPointer -= 2;

                @Pc(15) int local15 = intStack[intStackPointer];
                @Pc(21) int local21 = intStack[intStackPointer + 1];

                if (PlayerEntity.self.playerModel == null) {
                    return;
                }

                PlayerEntity.self.playerModel.setBaseColour(local15, local21);
                return;
            }

            if (op == SETGENDER) {
                @Pc(575) boolean female = intStack[--intStackPointer] != 0;

                if (PlayerEntity.self.playerModel == null) {
                    return;
                }

                PlayerEntity.self.playerModel.setFemale(female);
                return;
            }

            if (op == SETOBJ) {
                intStackPointer -= 2;

                @Pc(15) int part = intStack[intStackPointer];
                @Pc(21) int objId = intStack[intStackPointer + 1];

                if (PlayerEntity.self.playerModel == null) {
                    return;
                }

                PlayerEntity.self.playerModel.setObj(ObjTypeList.instance, part, objId);
                return;
            }
        } else if ((op >= 1000 && op < 1100) || (op >= 2000 && op < 2100)) {
            @Pc(220) Component component;
            if (op >= 2000) {
                op -= 1000;
                component = InterfaceList.list(intStack[--intStackPointer]);
            } else {
                component = secondary ? activeComponent2 : activeComponent;
            }

            if (op == CC_IF_SETPOSITION) {
                intStackPointer -= 4;

                component.originalX = intStack[intStackPointer];
                component.originalY = intStack[intStackPointer + 1];

                @Pc(21) int reposModeX = intStack[intStackPointer + 2];
                if (reposModeX < 0) {
                    reposModeX = 0;
                } else if (reposModeX > 5) {
                    reposModeX = 5;
                }

                @Pc(27) int reposModeY = intStack[intStackPointer + 3];
                if (reposModeY < 0) {
                    reposModeY = 0;
                } else if (reposModeY > 5) {
                    reposModeY = 5;
                }

                component.reposModeX = (byte) reposModeX;
                component.reposModeY = (byte) reposModeY;

                InterfaceManager.redraw(component);
                InterfaceManager.resizeAndReposition(component);

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetPosition(component.slot);
                }
                return;
            }

            if (op == CC_IF_SETSIZE) {
                intStackPointer -= 4;

                component.originalWidth = intStack[intStackPointer];
                component.originalHeight = intStack[intStackPointer + 1];

                component.modelAspectRatioX = 0;
                component.modelAspectRatioY = 0;

                @Pc(21) int resizeModeX = intStack[intStackPointer + 2];
                if (resizeModeX < 0) {
                    resizeModeX = 0;
                } else if (resizeModeX > 4) {
                    resizeModeX = 4;
                }

                @Pc(27) int resizeModeY = intStack[intStackPointer + 3];
                if (resizeModeY < 0) {
                    resizeModeY = 0;
                } else if (resizeModeY > 4) {
                    resizeModeY = 4;
                }

                component.resizeModeX = (byte) resizeModeX;
                component.resizeModeY = (byte) resizeModeY;

                InterfaceManager.redraw(component);
                InterfaceManager.resizeAndReposition(component);

                if (component.type == 0) {
                    InterfaceManager.calculateLayerDimensions(component, false);
                }
                return;
            }

            if (op == CC_IF_SETHIDE) {
                @Pc(834) boolean hidden = intStack[--intStackPointer] == 1;

                if (component.hidden != hidden) {
                    component.hidden = hidden;
                    InterfaceManager.redraw(component);
                }

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetHide(component.slot);
                }
                return;
            }

            if (op == CC_IF_SETASPECT) {
                intStackPointer -= 2;

                component.resizeAspectRatioY = intStack[intStackPointer];
                component.resizeAspectRatioX = intStack[intStackPointer + 1];

                InterfaceManager.redraw(component);
                InterfaceManager.resizeAndReposition(component);

                if (component.type == Component.TYPE_LAYER) {
                    InterfaceManager.calculateLayerDimensions(component, false);
                }
                return;
            }

            if (op == CC_IF_SETNOCLICKTHROUGH) {
                component.noClickThrough = intStack[--intStackPointer] == 1;
                return;
            }
        } else if (op >= 1100 && op < 1200 || !(op < 2100 || op >= 2200)) {
            @Pc(220) Component component;
            if (op >= 2000) {
                op -= 1000;
                component = InterfaceList.list(intStack[--intStackPointer]);
            } else {
                component = secondary ? activeComponent2 : activeComponent;
            }

            if (op == CC_IF_SETSCROLLPOS) {
                intStackPointer -= 2;

                component.scrollX = intStack[intStackPointer];
                if (component.scrollX > component.scrollWidth - component.width) {
                    component.scrollX = component.scrollWidth - component.width;
                }
                if (component.scrollX < 0) {
                    component.scrollX = 0;
                }

                component.scrollY = intStack[intStackPointer + 1];
                if (component.scrollY > component.scrollHeight - component.height) {
                    component.scrollY = component.scrollHeight - component.height;
                }
                if (component.scrollY < 0) {
                    component.scrollY = 0;
                }

                InterfaceManager.redraw(component);

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetScrollPosition(component.slot);
                }
                return;
            }

            if (op == CC_IF_SETCOLOUR) {
                component.colour = intStack[--intStackPointer];

                InterfaceManager.redraw(component);

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetColour(component.slot);
                }
                return;
            }

            if (op == CC_IF_SETFILL) {
                component.filled = intStack[--intStackPointer] == 1;
                InterfaceManager.redraw(component);
                return;
            }

            if (op == CC_IF_SETTTRANS) {
                component.transparency = intStack[--intStackPointer];
                InterfaceManager.redraw(component);
                return;
            }

            if (op == CC_IF_SETLINEWID) {
                component.lineWidth = intStack[--intStackPointer];
                InterfaceManager.redraw(component);
                return;
            }

            if (op == CC_IF_SETGRAPHIC) {
                @Pc(21) int graphic = intStack[--intStackPointer];

                if (component.graphic != graphic) {
                    component.graphic = graphic;
                    InterfaceManager.redraw(component);
                }

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetGraphic(component.slot);
                }
                return;
            }

            if (op == CC_IF_SET2DANGLE) {
                component.angle2d = intStack[--intStackPointer];
                InterfaceManager.redraw(component);
                return;
            }

            if (op == CC_IF_SETTILING) {
                component.tiling = intStack[--intStackPointer] == 1;
                InterfaceManager.redraw(component);
                return;
            }

            if (op == CC_IF_SETMODEL) {
                component.objType = 1;
                component.obj = intStack[--intStackPointer];

                InterfaceManager.redraw(component);

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetModel(component.slot);
                }
                return;
            }

            if (op == CC_IF_SETMODELANGLE) {
                intStackPointer -= 6;
                component.xof2d = intStack[intStackPointer];
                component.yof2d = intStack[intStackPointer + 1];
                component.xan2d = intStack[intStackPointer + 2];
                component.yan2d = intStack[intStackPointer + 3];
                component.zan2d = intStack[intStackPointer + 4];
                component.zoom2d = intStack[intStackPointer + 5];

                InterfaceManager.redraw(component);

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetModelAngle(component.slot);
                    DelayedStateChange.interfaceResetModelOffset(component.slot);
                }
                return;
            }

            if (op == CC_IF_SETMODELANIM) {
                @Pc(21) int animation = intStack[--intStackPointer];

                if (animation != component.modelAnimation) {
                    if (animation != -1) {
                        if (component.animator == null) {
                            component.animator = new ComponentAnimator();
                        }

                        component.animator.update(true, animation);
                    } else {
                        component.animator = null;
                    }

                    component.modelAnimation = animation;
                    InterfaceManager.redraw(component);
                }

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetModelAnim(component.slot);
                }
                return;
            }

            if (op == CC_IF_SETMODELORTHOG) {
                component.modelOrthog = intStack[--intStackPointer] == 1;
                InterfaceManager.redraw(component);
                return;
            }

            if (op == CC_IF_SETTEXT) {
                @Pc(1394) String text = stringStack[--stringStackPointer];

                if (!text.equals(component.text)) {
                    component.text = text;
                    InterfaceManager.redraw(component);
                }

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetText(component.slot);
                }
                return;
            }

            if (op == CC_IF_SETTEXTFONT) {
                component.fontGraphic = intStack[--intStackPointer];

                InterfaceManager.redraw(component);

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetTextFont(component.slot);
                }
                return;
            }

            if (op == CC_IF_SETTEXTALIGN) {
                intStackPointer -= 3;

                component.textAlignX = intStack[intStackPointer];
                component.textAlignY = intStack[intStackPointer + 1];
                component.textHeight = intStack[intStackPointer + 2];

                InterfaceManager.redraw(component);
                return;
            }

            if (op == CC_IF_SETTEXTSHADOW) {
                component.textShadow = intStack[--intStackPointer] == 1;
                InterfaceManager.redraw(component);
                return;
            }

            if (op == CC_IF_SETOUTLINE) {
                component.outline = intStack[--intStackPointer];
                InterfaceManager.redraw(component);
                return;
            }

            if (op == CC_IF_SETGRAPHICSHADOW) {
                component.graphicShadow = intStack[--intStackPointer];
                InterfaceManager.redraw(component);
                return;
            }

            if (op == CC_IF_SETHFLIP) {
                component.horizontalFlip = intStack[--intStackPointer] == 1;
                InterfaceManager.redraw(component);
                return;
            }

            if (op == CC_IF_SETVFLIP) {
                component.verticalFlip = intStack[--intStackPointer] == 1;
                InterfaceManager.redraw(component);
                return;
            }

            if (op == CC_IF_SETSCROLLSIZE) {
                intStackPointer -= 2;

                component.scrollWidth = intStack[intStackPointer];
                component.scrollHeight = intStack[intStackPointer + 1];

                InterfaceManager.redraw(component);

                if (component.type == 0) {
                    InterfaceManager.calculateLayerDimensions(component, false);
                }
                return;
            }

            if (op == CC_IF_SETALPHA) {
                component.alpha = intStack[--intStackPointer] == 1;
                InterfaceManager.redraw(component);
                return;
            }

            if (op == CC_IF_SETMODELZOOM) {
                component.zoom2d = intStack[--intStackPointer];

                InterfaceManager.redraw(component);

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetModelAngle(component.slot);
                }
                return;
            }

            if (op == CC_IF_SETLINEDIRECTION) {
                @Pc(21) int local21 = intStack[--intStackPointer];
                component.lineDirection = local21 == 1;
                InterfaceManager.redraw(component);
                return;
            }

            if (op == CC_IF_SETMODELORIGIN) {
                intStackPointer -= 2;
                component.modelOriginX = intStack[intStackPointer];
                component.modelOriginY = intStack[intStackPointer + 1];
                InterfaceManager.redraw(component);
                return;
            }

            if (op == CC_IF_SETMAXLINES) {
                component.maxLines = intStack[--intStackPointer];
                InterfaceManager.redraw(component);
                return;
            }

            if (op == CC_IF_SETPARAM_INT) {
                intStackPointer -= 2;

                @Pc(21) int id = intStack[intStackPointer];
                @Pc(27) int value = intStack[intStackPointer + 1];
                @Pc(1756) ParamType paramType = ParamTypeList.instance.list(id);

                if (value != paramType.defaultint) {
                    component.setParam(id, value);
                    return;
                }

                component.removeParam(id);
                return;
            }

            if (op == CC_IF_SETPARAM_STRING) {
                @Pc(21) int id = intStack[--intStackPointer];
                @Pc(1791) String value = stringStack[--stringStackPointer];
                @Pc(1756) ParamType paramType = ParamTypeList.instance.list(id);

                if (!paramType.defaultstr.equals(value)) {
                    component.setParam(value, id);
                    return;
                }

                component.removeParam(id);
                return;
            }

            if (op == 1129 || op == 1130) { // TODO: 1129=video graphic, 1130=video text?
                @Pc(21) int video = intStack[--intStackPointer];
                if ((component.type == Component.TYPE_GRAPHIC || op != 1129) && (component.type == Component.TYPE_TEXT || op != 1130)) {
                    if (component.video != video) {
                        component.video = video;
                        InterfaceManager.redraw(component);
                    }

                    if (component.id == -1) {
                        DelayedStateChange.interfaceResetVideo(component.slot);
                    }
                    return;
                }
                return;
            }

            if (op == CC_IF_SETRECOL) {
                intStackPointer -= 3;

                @Pc(21) int index = intStack[intStackPointer];
                @Pc(1892) short source = (short) intStack[intStackPointer + 1];
                @Pc(1899) short destination = (short) intStack[intStackPointer + 2];

                if (index >= 0 && index < 5) {
                    component.setRecol(destination, index, source);
                    InterfaceManager.redraw(component);

                    if (component.id == -1) {
                        DelayedStateChange.interfaceResetRecol(component.slot, index);
                    }
                    return;
                }

                return;
            }

            if (op == CC_IF_SETRETEX) {
                intStackPointer -= 3;

                @Pc(21) int index = intStack[intStackPointer];
                @Pc(1892) short source = (short) intStack[intStackPointer + 1];
                @Pc(1899) short destination = (short) intStack[intStackPointer + 2];

                if (index >= 0 && index < 5) {
                    component.setRetex(source, index, destination);
                    InterfaceManager.redraw(component);

                    if (component.id == -1) {
                        DelayedStateChange.interfaceResetRetex(component.slot, index);
                    }
                    return;
                }
                return;
            }

            if (op == CC_IF_SETFONTMONO) {
                component.fontMonospaced = intStack[--intStackPointer] == 1;
                InterfaceManager.redraw(component);

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetFontMono(component.slot);
                }
                return;
            }

            if (op == CC_IF_SETPARAM) {
                intStackPointer -= 2;

                @Pc(21) int id = intStack[intStackPointer];
                @Pc(27) int value = intStack[intStackPointer + 1];
                @Pc(1756) ParamType paramType = ParamTypeList.instance.list(id);

                if (value != paramType.defaultint) {
                    component.setParam(id, value);
                    return;
                }

                component.removeParam(id);
                return;
            }

            if (op == CC_IF_SETCLICKMASK) {
                component.clickMask = intStack[--intStackPointer] == 1;
                InterfaceManager.redraw(component);

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetClickmask(component.slot);
                }
                return;
            }
        } else if (op >= 1200 && op < 1300 || op >= 2200 && op < 2300) {
            @Pc(220) Component component;
            if (op >= 2000) {
                op -= 1000;
                component = InterfaceList.list(intStack[--intStackPointer]);
            } else {
                component = secondary ? activeComponent2 : activeComponent;
            }

            InterfaceManager.redraw(component);

            if (op == CC_IF_SETOBJECT || op == CC_IF_SETOBJECT_NONUM || op == CC_IF_SETOBJECT_WEARCOL || op == CC_IF_SETOBJECT_WEARCOL_NONUM || op == CC_IF_SETOBJECT_ALWAYSNUM || op == CC_IF_SETOBJECT_WEARCOL_ALWAYSNUM) {
                intStackPointer -= 2;

                @Pc(21) int invObject = intStack[intStackPointer];
                @Pc(27) int count = intStack[intStackPointer + 1];

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetObject(component.slot);
                    DelayedStateChange.interfaceResetModelAngle(component.slot);
                    DelayedStateChange.interfaceResetModelOffset(component.slot);
                }

                if (invObject == -1) {
                    component.objType = 1;
                    component.obj = -1;
                    component.invObject = -1;
                    return;
                }

                component.invObject = invObject;
                component.invCount = count;

                if (op == CC_IF_SETOBJECT_WEARCOL || op == CC_IF_SETOBJECT_WEARCOL_NONUM) {
                    component.objWearCol = true;
                } else {
                    component.objWearCol = false;
                }

                @Pc(2236) ObjType objType = ObjTypeList.instance.list(invObject);
                component.xan2d = objType.xan2d;
                component.yan2d = objType.yan2d;
                component.zan2d = objType.zan2d;
                component.xof2d = objType.xof2d;
                component.yof2d = objType.yof2d;
                component.zoom2d = objType.zoom2d;

                if (op == CC_IF_SETOBJECT_NONUM || op == CC_IF_SETOBJECT_WEARCOL_NONUM) {
                    component.objNumMode = ObjNumMode.SHOWCOUNT_NEVER;
                } else if (op == CC_IF_SETOBJECT_ALWAYSNUM || op == CC_IF_SETOBJECT_WEARCOL_ALWAYSNUM) {
                    component.objNumMode = ObjNumMode.SHOWCOUNT_ALWAYS;
                } else {
                    component.objNumMode = ObjNumMode.SHOWCOUNT_IFNOT1;
                }

                if (component.modelAspectRatioX > 0) {
                    component.zoom2d = component.zoom2d * 32 / component.modelAspectRatioX;
                    return;
                }
                if (component.originalWidth > 0) {
                    component.zoom2d = component.zoom2d * 32 / component.originalWidth;
                }
                return;
            }

            if (op == CC_IF_SETNPCHEAD) {
                component.objType = Component.OBJ_TYPE_NPCHEAD;
                component.obj = intStack[--intStackPointer];

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetModel(component.slot);
                }
                return;
            }

            if (op == CC_IF_SETPLAYERHEAD_SELF) {
                component.objType = Component.OBJ_TYPE_PLAYERHEAD;
                component.obj = -1;

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetModel(component.slot);
                }
                return;
            }

            if (op == CC_IF_SETNPCMODEL) {
                component.objType = Component.OBJ_TYPE_NPCMODEL;
                component.obj = intStack[--intStackPointer];

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetModel(component.slot);
                }
                return;
            }

            if (op == CC_IF_SETPLAYERMODEL) {
                component.objType = Component.OBJ_TYPE_PLAYERMODEL;
                component.obj = intStack[--intStackPointer];

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetModel(component.slot);
                }
                return;
            }

            if (op == 1206) {
                intStackPointer -= 4;

                component.skyBox = intStack[intStackPointer];
                component.skyBoxSphereOffsetX = intStack[intStackPointer + 1];
                component.skyBoxSphereOffsetY = intStack[intStackPointer + 2];
                component.skyBoxSphereOffsetZ = intStack[intStackPointer + 3];

                InterfaceManager.redraw(component);
                return;
            }

            if (op == 1207) {
                intStackPointer -= 2;

                component.skyboxRenderPitch = intStack[intStackPointer];
                component.skyboxRenderYaw = intStack[intStackPointer + 1];

                InterfaceManager.redraw(component);
                return;
            }

            if (op == CC_IF_SETOBJECT_DATA) {
                intStackPointer -= 4;

                component.obj = intStack[intStackPointer];
                component.objData = intStack[intStackPointer + 1];

                if (intStack[intStackPointer + 2] == 1) {
                    component.objType = Component.OBJ_TYPE_INVENTORY_FEMALE;
                } else {
                    component.objType = Component.OBJ_TYPE_INVENTORY_MALE;
                }

                if (intStack[intStackPointer + 3] == 1) {
                    component.objWearCol = true;
                } else {
                    component.objWearCol = false;
                }

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetModel(component.slot);
                }
                return;
            }

            if (op == CC_IF_SETPLAYERMODEL_SELF) {
                component.objType = Component.OBJ_TYPE_PLAYERMODEL;
                component.obj = PlayerList.activePlayerSlot;
                component.objData = 0;

                if (component.id == -1) {
                    DelayedStateChange.interfaceResetModel(component.slot);
                }
                return;
            }
        } else if (op >= 1300 && op < 1400 || op >= 2300 && op < 2400) {
            @Pc(220) Component component;
            if (op >= 2000) {
                op -= 1000;
                component = InterfaceList.list(intStack[--intStackPointer]);
            } else {
                component = secondary ? activeComponent2 : activeComponent;
            }

            if (op == CC_IF_SETOP) {
                @Pc(21) int opNum = intStack[--intStackPointer] - 1;

                if (opNum >= 0 && opNum <= 9) {
                    component.setOp(opNum, stringStack[--stringStackPointer]);
                    return;
                }

                stringStackPointer--;
                return;
            }

            if (op == CC_IF_SETDRAGGABLE) {
                intStackPointer -= 2;

                @Pc(21) int idAndSlot = intStack[intStackPointer];
                @Pc(27) int id = intStack[intStackPointer + 1];

                if (idAndSlot == -1 && id == -1) {
                    component.dragLayer = null;
                    return;
                }

                component.dragLayer = InterfaceList.getComponent(idAndSlot, id);
                return;
            }

            if (op == CC_IF_SETDRAGRENDERBEHAVIOUR) {
                @Pc(21) int behaviour = intStack[--intStackPointer];
                if (behaviour != DragRenderBehaviour.OFFSET_TRANSPARENT && behaviour != DragRenderBehaviour.FIXED && behaviour != DragRenderBehaviour.OFFSET) {
                    return;
                }

                component.dragRenderBehaviour = behaviour;
                return;
            }

            if (op == CC_IF_SETDRAGDEADZONE) {
                component.dragDeadZone = intStack[--intStackPointer];
                return;
            }

            if (op == CC_IF_SETDRAGDEADTIME) {
                component.dragDeadTime = intStack[--intStackPointer];
                return;
            }

            if (op == CC_IF_SETOPBASE) {
                component.opBase = stringStack[--stringStackPointer];
                return;
            }

            if (op == CC_IF_SETTARGETVERB) {
                component.targetVerb = stringStack[--stringStackPointer];
                return;
            }

            if (op == CC_IF_CLEAROPS) {
                component.ops = null;
                return;
            }

            if (op == CC_IF_SETTARGETCURSORS) {
                component.targetEndCursor = intStack[--intStackPointer];
                component.targetEnterCursor = intStack[--intStackPointer];
                return;
            }

            if (op == CC_IF_SETOPCURSOR) {
                @Pc(21) int cursor = intStack[--intStackPointer];
                @Pc(27) int opNum = intStack[--intStackPointer];

                if (opNum >= 1 && opNum <= 10) {
                    component.setOpCursor(opNum - 1, cursor);
                }
                return;
            }

            if (op == CC_IF_SETPAUSETEXT) {
                component.pauseText = stringStack[--stringStackPointer];
                return;
            }

            if (op == CC_IF_SETTARGETOPCURSOR) {
                component.targetOpCursor = intStack[--intStackPointer];
                return;
            }

            if (op == CC_IF_SETOPCHAR || op == CC_IF_SETOPKEY) {
                @Pc(21) int index;
                @Pc(27) int opKey;
                @Pc(506) int opChar;

                if (op == CC_IF_SETOPCHAR) {
                    intStackPointer -= 3;

                    index = intStack[intStackPointer] - 1;
                    opKey = intStack[intStackPointer + 1];
                    opChar = intStack[intStackPointer + 2];

                    if (index < 0 || index > 9) {
                        throw new RuntimeException("IOR13121313");
                    }
                } else {
                    intStackPointer -= 2;

                    index = 10;
                    opKey = intStack[intStackPointer];
                    opChar = intStack[intStackPointer + 1];
                }

                if (component.opKeys == null) {
                    if (opKey == 0) {
                        return;
                    }

                    component.opKeys = new byte[11];
                    component.opChars = new byte[11];
                    component.opKeyRates = new int[11];
                }

                component.opKeys[index] = (byte) opKey;

                if (opKey == 0) {
                    component.hasOpKey = false;

                    for (@Pc(2978) int i = 0; i < component.opKeys.length; i++) {
                        if (component.opKeys[i] != 0) {
                            component.hasOpKey = true;
                            break;
                        }
                    }
                } else {
                    component.hasOpKey = true;
                }

                component.opChars[index] = (byte) opChar;
                return;
            }

            if (op == CC_IF_SETMOUSEOVERCURSOR) {
                component.mouseOverCursor = intStack[--intStackPointer];
                return;
            }
        } else if (op >= 1400 && op < 1500 || op >= 2400 && op < 2500) {
            @Pc(220) Component component;
            if (op >= 2000) {
                op -= 1000;
                component = InterfaceList.list(intStack[--intStackPointer]);
            } else {
                component = secondary ? activeComponent2 : activeComponent;
            }

            if (op == CC_IF_CLEARSCRIPTHOOKS) {
                component.clearScriptHooks();
                return;
            }

            @Pc(1394) String argTypes = stringStack[--stringStackPointer];
            @Pc(3077) int[] triggers = null;
            if (argTypes.length() > 0 && argTypes.charAt(argTypes.length() - 1) == 'Y') {
                @Pc(506) int length = intStack[--intStackPointer];

                if (length > 0) {
                    triggers = new int[length];

                    while (length-- > 0) {
                        triggers[length] = intStack[--intStackPointer];
                    }
                }

                argTypes = argTypes.substring(0, argTypes.length() - 1);
            }

            @Pc(3131) Object[] args = new Object[argTypes.length() + 1];
            for (@Pc(2978) int i = args.length - 1; i >= 1; i--) {
                if (argTypes.charAt(i - 1) == 's') {
                    args[i] = stringStack[--stringStackPointer];
                } else if (argTypes.charAt(i - 1) == '§') {
                    args[i] = Long.valueOf(longStack[--longStackPointer]);
                } else {
                    args[i] = Integer.valueOf(intStack[--intStackPointer]);
                }
            }

            @Pc(72) int scriptId = intStack[--intStackPointer];
            if (scriptId == -1) {
                args = null;
            } else {
                args[0] = Integer.valueOf(scriptId);
            }

            if (op == CC_IF_SETONCLICK) {
                component.onClick = args;
            } else if (op == CC_IF_SETONHOLD) {
                component.onHold = args;
            } else if (op == CC_IF_SETONRELEASE) {
                component.onRelease = args;
            } else if (op == CC_IF_SETONMOUSEOVER) {
                component.onMouseOver = args;
            } else if (op == CC_IF_SETONMOUSELEAVE) {
                component.onMouseLeave = args;
            } else if (op == CC_IF_SETONDRAG) {
                component.onDrag = args;
            } else if (op == CC_IF_SETONTARGETLEAVE) {
                component.onTargetLeave = args;
            } else if (op == CC_IF_SETONVARTRANSMIT) {
                component.onVarTransmit = args;
                component.varpTriggers = triggers;
            } else if (op == CC_IF_SETONTIMER) {
                component.onTimer = args;
            } else if (op == CC_IF_SETONOP) {
                component.onOp = args;
            } else if (op == CC_IF_SETONDRAGCOMPLETE) {
                component.onDragComplete = args;
            } else if (op == CC_IF_SETONCLICKREPEAT) {
                component.onClickRepeat = args;
            } else if (op == CC_IF_SETONMOUSEREPEAT) {
                component.onMouseRepeat = args;
            } else if (op == CC_IF_SETONINVTRANSMIT) {
                component.onInvTransmit = args;
                component.inventoryTriggers = triggers;
            } else if (op == CC_IF_SETONSTATTRANSMIT) {
                component.onStatTransmit = args;
                component.statTriggers = triggers;
            } else if (op == CC_IF_SETONTARGETENTER) {
                component.onTargetEnter = args;
            } else if (op == CC_IF_SETONSCROLLWHEEL) {
                component.onScrollWheel = args;
            } else if (op == CC_IF_SETONCHATTRANSMIT) {
                component.onChatTransmit = args;
            } else if (op == CC_IF_SETONKEY) {
                component.onKey = args;
            } else if (op == CC_IF_SETONFRIENDTRANSMIT) {
                component.onFriendTransmit = args;
            } else if (op == CC_IF_SETONCLANTRANSMIT) {
                component.onClanTransmit = args;
            } else if (op == CC_IF_SETONMISCTRANSMIT) {
                component.onMiscTransmit = args;
            } else if (op == CC_IF_SETONDIALOGABORT) {
                component.onDialogAbort = args;
            } else if (op == CC_IF_SETONSUBCHANGE) {
                component.onSubChange = args;
            } else if (op == CC_IF_SETONSTOCKTRANSMIT) {
                component.onStockTransmit = args;
            } else if (op == CC_IF_SETONCAMFINISHED) {
                component.onCamFinished = args;
            } else if (op == CC_IF_SETONRESIZE) {
                component.onResize = args;
            } else if (op == CC_IF_SETONVARCTRANSMIT) {
                component.onVarcTransmit = args;
                component.varcTriggers = triggers;
            } else if (op == CC_IF_SETONVARCSTRTRANSMIT) {
                component.onVarcstrTransmit = args;
                component.varcstrTriggers = triggers;
            } else if (op == CC_IF_SETONOPT) {
                component.onOpT = args;
            } else if (op == CC_IF_SETONCLANSETTINGSTRANSMIT) {
                component.onClanSettingsTransmit = args;
            } else if (op == CC_IF_SETONCLANCHANNELTRANSMIT) {
                component.onClanChannelTransmit = args;
            } else if (op == CC_IF_SETONVARCLANTRANSMIT) {
                component.onVarclanTransmit = args;
            }

            component.hasHook = true;
            return;
        } else if (op < 1600) {
            @Pc(220) Component cc = secondary ? activeComponent2 : activeComponent;

            if (op == CC_GETX) {
                intStack[intStackPointer++] = cc.x;
                return;
            }
            if (op == CC_GETY) {
                intStack[intStackPointer++] = cc.y;
                return;
            }
            if (op == CC_GETWIDTH) {
                intStack[intStackPointer++] = cc.width;
                return;
            }
            if (op == CC_GETHEIGHT) {
                intStack[intStackPointer++] = cc.height;
                return;
            }
            if (op == CC_GETHIDE) {
                intStack[intStackPointer++] = cc.hidden ? 1 : 0;
                return;
            }
            if (op == CC_GETLAYER) {
                intStack[intStackPointer++] = cc.layer;
                return;
            }
            if (op == CC_GETPARENTLAYER) {
                @Pc(248) Component parent = InterfaceManager.getParentLayer(cc);
                intStack[intStackPointer++] = parent == null ? -1 : parent.slot;
                return;
            }
            if (op == CC_GETCOLOUR) {
                intStack[intStackPointer++] = cc.colour;
                return;
            }
        } else if (op < 1700) {
            @Pc(220) Component cc = secondary ? activeComponent2 : activeComponent;

            if (op == CC_GETSCROLLX) {
                intStack[intStackPointer++] = cc.scrollX;
                return;
            }
            if (op == CC_GETSCROLLY) {
                intStack[intStackPointer++] = cc.scrollY;
                return;
            }
            if (op == CC_GETTEXT) {
                stringStack[stringStackPointer++] = cc.text;
                return;
            }
            if (op == CC_GETSCROLLWIDTH) {
                intStack[intStackPointer++] = cc.scrollWidth;
                return;
            }
            if (op == CC_GETSCROLLHEIGHT) {
                intStack[intStackPointer++] = cc.scrollHeight;
                return;
            }
            if (op == CC_GETMODELZOOM) {
                intStack[intStackPointer++] = cc.zoom2d;
                return;
            }
            if (op == CC_GETMODELANGLE_X) {
                intStack[intStackPointer++] = cc.xan2d;
                return;
            }
            if (op == CC_GETMODELANGLE_Z) {
                intStack[intStackPointer++] = cc.zan2d;
                return;
            }
            if (op == CC_GETMODELANGLE_Y) {
                intStack[intStackPointer++] = cc.yan2d;
                return;
            }
            if (op == CC_GETTRANS) {
                intStack[intStackPointer++] = cc.transparency;
                return;
            }
            if (op == CC_GETMODELXOF) {
                intStack[intStackPointer++] = cc.xof2d;
                return;
            }
            if (op == CC_GETMODELYOF) {
                intStack[intStackPointer++] = cc.yof2d;
                return;
            }
            if (op == CC_GETGRAPHIC) {
                intStack[intStackPointer++] = cc.graphic;
                return;
            }
            if (op == CC_PARAM) {
                @Pc(21) int id = intStack[--intStackPointer];
                @Pc(3848) ParamType paramType = ParamTypeList.instance.list(id);

                if (paramType.isString()) {
                    stringStack[stringStackPointer++] = cc.param(paramType.defaultstr, id);
                    return;
                }

                intStack[intStackPointer++] = cc.param(paramType.defaultint, id);
                return;
            }
            if (op == CC_GET2DANGLE) {
                intStack[intStackPointer++] = cc.angle2d;
                return;
            }
            if (op == CC_GETMODEL) {
                intStack[intStackPointer++] = cc.objType == Component.OBJ_TYPE_MODEL ? cc.obj : -1;
                return;
            }
            if (op == CC_GETFONTGRAPHIC) {
                intStack[intStackPointer++] = cc.fontGraphic;
                return;
            }
        } else if (op < 1800) {
            @Pc(220) Component cc = secondary ? activeComponent2 : activeComponent;

            if (op == CC_GETINVOBJECT) {
                intStack[intStackPointer++] = cc.invObject;
                return;
            }
            if (op == CC_GETINVCOUNT) {
                if (cc.invObject != -1) {
                    intStack[intStackPointer++] = cc.invCount;
                    return;
                }
                intStack[intStackPointer++] = 0;
                return;
            }
            if (op == CC_GETID) {
                intStack[intStackPointer++] = cc.id;
                return;
            }
        } else if (op < 1900) {
            @Pc(220) Component cc = secondary ? activeComponent2 : activeComponent;

            if (op == CC_GETTARGETMASK) {
                intStack[intStackPointer++] = InterfaceManager.serverActiveProperties(cc).getTargetMask();
                return;
            }

            if (op == CC_GETOP) {
                @Pc(21) int opNum = intStack[--intStackPointer];
                opNum--;

                if (cc.ops != null && opNum < cc.ops.length && cc.ops[opNum] != null) {
                    stringStack[stringStackPointer++] = cc.ops[opNum];
                    return;
                }

                stringStack[stringStackPointer++] = "";
                return;
            }

            if (op == CC_GETOPBASE) {
                if (cc.opBase == null) {
                    stringStack[stringStackPointer++] = "";
                    return;
                }

                stringStack[stringStackPointer++] = cc.opBase;
                return;
            }
        } else if (op < 2000 || op >= 2900 && op < 3000) {
            @Pc(220) Component component;
            if (op >= 2000) {
                component = InterfaceList.list(intStack[--intStackPointer]);
                op -= 1000;
            } else {
                component = secondary ? activeComponent2 : activeComponent;
            }

            if (lastHookId >= 10) {
                throw new RuntimeException("C29xx-1");
            }

            if (op == CC_IF_CALLONRESIZE) {
                if (component.onResize == null) {
                    return;
                }

                @Pc(4169) HookRequest hook = new HookRequest();
                hook.source = component;
                hook.arguments = component.onResize;
                hook.id = lastHookId + 1;
                InterfaceManager.hookRequests.addLast(hook);
                return;
            }
        } else if (op < 2600) {
            @Pc(220) Component component = InterfaceList.list(intStack[--intStackPointer]);

            if (op == IF_GETX) {
                intStack[intStackPointer++] = component.x;
                return;
            }
            if (op == IF_GETY) {
                intStack[intStackPointer++] = component.y;
                return;
            }
            if (op == IF_GETWIDTH) {
                intStack[intStackPointer++] = component.width;
                return;
            }
            if (op == IF_GETHEIGHT) {
                intStack[intStackPointer++] = component.height;
                return;
            }
            if (op == IF_GETHIDE) {
                intStack[intStackPointer++] = component.hidden ? 1 : 0;
                return;
            }
            if (op == IF_GETLAYER) {
                intStack[intStackPointer++] = component.layer;
                return;
            }
            if (op == IF_GETPARENTLAYER) {
                @Pc(248) Component parent = InterfaceManager.getParentLayer(component);
                intStack[intStackPointer++] = parent == null ? -1 : parent.slot;
                return;
            }
            if (op == IF_GETCOLOUR) {
                intStack[intStackPointer++] = component.colour;
                return;
            }
        } else if (op < 2700) {
            @Pc(220) Component component = InterfaceList.list(intStack[--intStackPointer]);

            if (op == IF_GETSCROLLX) {
                intStack[intStackPointer++] = component.scrollX;
                return;
            }
            if (op == IF_GETSCROLLY) {
                intStack[intStackPointer++] = component.scrollY;
                return;
            }
            if (op == IF_GETTEXT) {
                stringStack[stringStackPointer++] = component.text;
                return;
            }
            if (op == IF_GETSCROLLWIDTH) {
                intStack[intStackPointer++] = component.scrollWidth;
                return;
            }
            if (op == IF_GETSCROLLHEIGHT) {
                intStack[intStackPointer++] = component.scrollHeight;
                return;
            }
            if (op == IF_GETMODELZOOM) {
                intStack[intStackPointer++] = component.zoom2d;
                return;
            }
            if (op == IF_GETMODELANGLE_X) {
                intStack[intStackPointer++] = component.xan2d;
                return;
            }
            if (op == IF_GETMODELANGLE_Z) {
                intStack[intStackPointer++] = component.zan2d;
                return;
            }
            if (op == IF_GETMODELANGLE_Y) {
                intStack[intStackPointer++] = component.yan2d;
                return;
            }
            if (op == IF_GETTRANS) {
                intStack[intStackPointer++] = component.transparency;
                return;
            }
            if (op == IF_GETMODELXOF) {
                intStack[intStackPointer++] = component.xof2d;
                return;
            }
            if (op == IF_GETMODELYOF) {
                intStack[intStackPointer++] = component.yof2d;
                return;
            }
            if (op == IF_GETGRAPHIC) {
                intStack[intStackPointer++] = component.graphic;
                return;
            }
            if (op == IF_GET2DANGLE) {
                intStack[intStackPointer++] = component.angle2d;
                return;
            }
            if (op == IF_GETMODEL) {
                intStack[intStackPointer++] = component.objType == Component.OBJ_TYPE_MODEL ? component.obj : -1;
                return;
            }
            if (op == IF_GETFONTGRAPHIC) {
                intStack[intStackPointer++] = component.fontGraphic;
                return;
            }
        } else if (op < 2800) {
            if (op == IF_GETINVOBJECT) {
                @Pc(220) Component component = InterfaceList.list(intStack[--intStackPointer]);
                intStack[intStackPointer++] = component.invObject;
                return;
            }

            if (op == IF_GETINVCOUNT) {
                @Pc(220) Component component = InterfaceList.list(intStack[--intStackPointer]);

                if (component.invObject != -1) {
                    intStack[intStackPointer++] = component.invCount;
                    return;
                }

                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == IF_HASSUB) {
                @Pc(15) int id = intStack[--intStackPointer];
                @Pc(4653) SubInterface sub = (SubInterface) InterfaceManager.subInterfaces.get(id);

                if (sub != null) {
                    intStack[intStackPointer++] = 1;
                    return;
                }

                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == IF_GETNEXTSUBID) {
                @Pc(220) Component component = InterfaceList.list(intStack[--intStackPointer]);
                if (component.staticComponents == null) {
                    intStack[intStackPointer++] = 0;
                    return;
                }

                @Pc(21) int next = component.staticComponents.length;
                for (@Pc(27) int i = 0; i < component.staticComponents.length; i++) {
                    if (component.staticComponents[i] == null) {
                        next = i;
                        break;
                    }
                }

                intStack[intStackPointer++] = next;
                return;
            }

            if (op == IF_HASSUBMODAL || op == IF_HASSUBOVERLAY) {
                intStackPointer -= 2;

                @Pc(15) int id = intStack[intStackPointer];
                @Pc(21) int subId = intStack[intStackPointer + 1];
                @Pc(4760) SubInterface sub = (SubInterface) InterfaceManager.subInterfaces.get(id);

                if (sub != null && sub.id == subId) {
                    intStack[intStackPointer++] = 1;
                    return;
                }

                intStack[intStackPointer++] = 0;
                return;
            }
        } else if (op < 2900) {
            @Pc(220) Component component = InterfaceList.list(intStack[--intStackPointer]);

            if (op == IF_GETTARGETMASK) {
                intStack[intStackPointer++] = InterfaceManager.serverActiveProperties(component).getTargetMask();
                return;
            }

            if (op == IF_GETOP) {
                @Pc(21) int opNum = intStack[--intStackPointer];
                opNum--;

                if (component.ops != null && opNum < component.ops.length && component.ops[opNum] != null) {
                    stringStack[stringStackPointer++] = component.ops[opNum];
                    return;
                }

                stringStack[stringStackPointer++] = "";
                return;
            }

            if (op == IF_GETOPBASE) {
                if (component.opBase == null) {
                    stringStack[stringStackPointer++] = "";
                    return;
                }
                stringStack[stringStackPointer++] = component.opBase;
                return;
            }
        } else if (op < 3200) {
            if (op == MES) {
                @Pc(4911) String message = stringStack[--stringStackPointer];
                ChatHistory.addScript(message);
                return;
            }

            if (op == 3101) {
                intStackPointer -= 2;
                PlayerEntity.animate(PlayerEntity.self, intStack[intStackPointer + 1], intStack[intStackPointer]);
                return;
            }

            if (op == IF_CLOSE) {
                InterfaceManager.close();
                return;
            }

            if (op == RESUME_COUNTDIALOG) {
                @Pc(4911) String input = stringStack[--stringStackPointer];

                @Pc(21) int count = 0;
                if (StringTools.isDecimal(input)) {
                    count = StringTools.parseDecimal(input);
                }

                @Pc(4974) ClientMessage message = ClientMessage.create(ClientProt.RESUME_P_COUNTDIALOG, ServerConnection.GAME.isaac);
                message.bitPacket.p4(count);
                ServerConnection.GAME.send(message);
                return;
            }

            if (op == RESUME_STRINGDIALOG) {
                @Pc(4911) String input = stringStack[--stringStackPointer];
                @Pc(5005) ClientMessage message = ClientMessage.create(ClientProt.RESUME_P_STRINGDIALOG, ServerConnection.GAME.isaac);
                message.bitPacket.p1(input.length() + 1);
                message.bitPacket.pjstr(input);
                ServerConnection.GAME.send(message);
                return;
            }

            if (op == RESUME_NAMEDIALOG) {
                @Pc(4911) String name = stringStack[--stringStackPointer];
                @Pc(5005) ClientMessage message = ClientMessage.create(ClientProt.RESUME_P_NAMEDIALOG, ServerConnection.GAME.isaac);
                message.bitPacket.p1(name.length() + 1);
                message.bitPacket.pjstr(name);
                ServerConnection.GAME.send(message);
                return;
            }

            if (op == OPPLAYER) {
                @Pc(15) int local15 = intStack[--intStackPointer];
                @Pc(1394) String local1394 = stringStack[--stringStackPointer];
                ThreeDView.doOpPlayer(local1394, local15);
                return;
            }

            if (op == IF_DRAGPICKUP) {
                intStackPointer -= 3;

                @Pc(15) int x = intStack[intStackPointer];
                @Pc(21) int y = intStack[intStackPointer + 1];
                @Pc(27) int id = intStack[intStackPointer + 2];
                @Pc(38) Component component = InterfaceList.list(id);

                InterfaceManager.dragTryPickup(component, x, y);
                return;
            }

            if (op == CC_DRAGPICKUP) {
                intStackPointer -= 2;

                @Pc(15) int x = intStack[intStackPointer];
                @Pc(21) int y = intStack[intStackPointer + 1];
                @Pc(303) Component cc = secondary ? activeComponent2 : activeComponent;

                InterfaceManager.dragTryPickup(cc, x, y);
                return;
            }

            if (op == RESUME_OBJDIALOG) {
                @Pc(15) int objId = intStack[--intStackPointer];
                @Pc(5005) ClientMessage message = ClientMessage.create(ClientProt.RESUME_P_OBJDIALOG, ServerConnection.GAME.isaac);
                message.bitPacket.p2(objId);
                ServerConnection.GAME.send(message);
                return;
            }

            if (op == IF_OPENSUBCLIENT) {
                intStackPointer -= 2;

                @Pc(15) int idAndSlot = intStack[intStackPointer];
                @Pc(21) int subId = intStack[intStackPointer + 1];
                @Pc(4760) SubInterface sub = (SubInterface) InterfaceManager.subInterfaces.get(idAndSlot);

                if (sub != null) {
                    InterfaceManager.closeSubInterface(sub, sub.id != subId, true);
                }

                InterfaceManager.openSubInterface(3, subId, idAndSlot, true);
                return;
            }

            if (op == IF_CLOSESUBCLIENT) {
                intStackPointer--;

                @Pc(15) int idAndSlot = intStack[intStackPointer];
                @Pc(4653) SubInterface sub = (SubInterface) InterfaceManager.subInterfaces.get(idAndSlot);

                if (sub != null && sub.type == 3) {
                    InterfaceManager.closeSubInterface(sub, true, true);
                }
                return;
            }

            if (op == OPPLAYERT) {
                ThreeDView.doTargetPlayer(stringStack[--stringStackPointer]);
                return;
            }

            if (op == MES_TYPED) {
                intStackPointer -= 2;

                @Pc(15) int type = intStack[intStackPointer];
                @Pc(21) int flags = intStack[intStackPointer + 1];
                @Pc(1791) String message = stringStack[--stringStackPointer];

                ChatHistory.add(type, flags, "", "", "", message);
                return;
            }

            if (op == SETUP_MESSAGEBOX) {
                intStackPointer -= 11;

                @Pc(5320) HorizontalAlignment[] horizontalAlignments = HorizontalAlignment.values();
                @Pc(5323) VerticalAlignment[] verticalAlignments = VerticalAlignment.values();
                MessageBox.setup(horizontalAlignments[intStack[intStackPointer]], verticalAlignments[intStack[intStackPointer + 1]], intStack[intStackPointer + 2], intStack[intStackPointer + 3], intStack[intStackPointer + 4], intStack[intStackPointer + 5], intStack[intStackPointer + 6], intStack[intStackPointer + 7], intStack[intStackPointer + 8], intStack[intStackPointer + 9], intStack[intStackPointer + 10]);
                return;
            }

            if (op == RESUME_HSLDIALOG) {
                @Pc(15) int hsl = intStack[--intStackPointer];
                @Pc(5005) ClientMessage message = ClientMessage.create(ClientProt.RESUME_P_HSLDIALOG, ServerConnection.GAME.isaac);
                message.bitPacket.p2(hsl);
                ServerConnection.GAME.send(message);
                return;
            }

            if (op == RESUME_CLANFORUMQFCDIALOG) {
                @Pc(4911) String clanForumQfc = stringStack[--stringStackPointer];
                @Pc(5005) ClientMessage message = ClientMessage.create(ClientProt.A_CLIENT_PROT___82, ServerConnection.GAME.isaac);
                message.bitPacket.p1(clanForumQfc.length() + 1);
                message.bitPacket.pjstr(clanForumQfc);
                ServerConnection.GAME.send(message);
                return;
            }
        } else if (op < 3300) {
            if (op == SOUND_SYNTH) {
                intStackPointer -= 3;
                SoundManager.playSynthSound(intStack[intStackPointer], intStack[intStackPointer + 1], intStack[intStackPointer + 2], 255, 256);
                return;
            }
            if (op == SOUND_SONG) {
                SoundManager.playMidiSong(intStack[--intStackPointer], 255, 50);
                return;
            }
            if (op == SOUND_JINGLE) {
                intStackPointer -= 2;
                SoundManager.playMidiJingle(intStack[intStackPointer], intStack[intStackPointer + 1], 255);
                return;
            }
            if (op == SOUND_SYTHN_VOLUME) {
                intStackPointer -= 4;
                SoundManager.playSynthSound(intStack[intStackPointer], intStack[intStackPointer + 1], intStack[intStackPointer + 2], intStack[intStackPointer + 3], 256);
                return;
            }
            if (op == SOUND_SONG_VOLUME) {
                intStackPointer -= 3;
                SoundManager.playMidiSong(intStack[intStackPointer], intStack[intStackPointer + 1], intStack[intStackPointer + 2]);
                return;
            }
            if (op == SOUND_JINGLE_VOLUME) {
                intStackPointer -= 3;
                SoundManager.playMidiJingle(intStack[intStackPointer], intStack[intStackPointer + 1], intStack[intStackPointer + 2]);
                return;
            }
            if (op == SOUND_VORBIS_VOLUME) {
                intStackPointer -= 4;
                SoundManager.playVorbisSound(intStack[intStackPointer], intStack[intStackPointer + 1], intStack[intStackPointer + 2], intStack[intStackPointer + 3], 256, false);
                return;
            }
            if (op == SOUND_SPEECH_VOLUME) {
                intStackPointer -= 4;
                SoundManager.playVorbisSound(intStack[intStackPointer], intStack[intStackPointer + 1], intStack[intStackPointer + 2], intStack[intStackPointer + 3], 256, true);
                return;
            }
            if (op == SOUND_SYNTH_RATE) {
                intStackPointer -= 5;
                SoundManager.playSynthSound(intStack[intStackPointer], intStack[intStackPointer + 1], intStack[intStackPointer + 2], intStack[intStackPointer + 3], intStack[intStackPointer + 4]);
                return;
            }
            if (op == SOUND_VORBIS_RATE) {
                intStackPointer -= 5;
                SoundManager.playVorbisSound(intStack[intStackPointer], intStack[intStackPointer + 1], intStack[intStackPointer + 2], intStack[intStackPointer + 3], intStack[intStackPointer + 4], false);
                return;
            }
        } else if (op < 3400) {
            if (op == CLIENTCLOCK) {
                intStack[intStackPointer++] = TimeUtils.clock;
                return;
            }

            if (op == INV_GETOBJ) {
                intStackPointer -= 2;

                @Pc(15) int id = intStack[intStackPointer];
                @Pc(21) int slot = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = ClientInventory.get(id, slot, false);
                return;
            }

            if (op == INV_GETNUM) {
                intStackPointer -= 2;

                @Pc(15) int id = intStack[intStackPointer];
                @Pc(21) int slot = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = ClientInventory.count(id, slot, false);
                return;
            }

            if (op == INV_TOTAL) {
                intStackPointer -= 2;

                @Pc(15) int id = intStack[intStackPointer];
                @Pc(21) int slot = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = ClientInventory.total(id, slot, false);
                return;
            }

            if (op == INV_SIZE) {
                @Pc(15) int id = intStack[--intStackPointer];
                intStack[intStackPointer++] = InvTypeList.instance.list(id).size;
                return;
            }

            if (op == STAT) {
                @Pc(15) int skill = intStack[--intStackPointer];
                intStack[intStackPointer++] = Static581.statLevels[skill];
                return;
            }

            if (op == STAT_BASE) {
                @Pc(15) int skill = intStack[--intStackPointer];
                intStack[intStackPointer++] = Static498.statBaseLevels[skill];
                return;
            }

            if (op == STAT_VISIBLE_XP) {
                @Pc(15) int skill = intStack[--intStackPointer];
                intStack[intStackPointer++] = Static237.statXps[skill];
                return;
            }

            if (op == COORD) {
                @Pc(5929) byte level = PlayerEntity.self.level;
                @Pc(21) int x = (PlayerEntity.self.x >> 9) + WorldMap.areaBaseX;
                @Pc(27) int z = (PlayerEntity.self.z >> 9) + WorldMap.areaBaseZ;
                intStack[intStackPointer++] = (level << 28) + (x << 14) + z;
                return;
            }

            if (op == COORDX) {
                @Pc(15) int x = intStack[--intStackPointer];
                intStack[intStackPointer++] = (x >> 14) & 0x3FFF;
                return;
            }

            if (op == COORDY) {
                @Pc(15) int y = intStack[--intStackPointer];
                intStack[intStackPointer++] = y >> 28;
                return;
            }

            if (op == COORDZ) {
                @Pc(15) int z = intStack[--intStackPointer];
                intStack[intStackPointer++] = z & 0x3FFF;
                return;
            }

            if (op == MAP_MEMBERS) {
                intStack[intStackPointer++] = Static174.mapMembers ? 1 : 0;
                return;
            }

            if (op == INVOTHER_GETOBJ) {
                intStackPointer -= 2;

                @Pc(15) int id = intStack[intStackPointer];
                @Pc(21) int slot = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = ClientInventory.get(id, slot, true);
                return;
            }

            if (op == INVOTHER_GETNUM) {
                intStackPointer -= 2;

                @Pc(15) int id = intStack[intStackPointer];
                @Pc(21) int slot = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = ClientInventory.count(id, slot, true);
                return;
            }

            if (op == INVOTHER_TOTAL) {
                intStackPointer -= 2;

                @Pc(15) int id = intStack[intStackPointer];
                @Pc(21) int slot = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = ClientInventory.total(id, slot, true);
                return;
            }

            if (op == STAFFMODLEVEL) {
                if (Client.staffModLevel >= 2) {
                    intStack[intStackPointer++] = Client.staffModLevel;
                    return;
                }
                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == REBOOTTIMER) {
                intStack[intStackPointer++] = Static249.rebootTimer;
                return;
            }

            if (op == MAP_WORLD) {
                intStack[intStackPointer++] = ConnectionInfo.login.world;
                return;
            }

            if (op == RUNENERGY_VISIBLE) {
                intStack[intStackPointer++] = Static703.runEnergy;
                return;
            }

            if (op == RUNWEIGHT_VISIBLE) {
                intStack[intStackPointer++] = Static494.runWeight;
                return;
            }

            if (op == PLAYERMOD) {
                if (Static38.playerModLevel >= 5 && Static38.playerModLevel <= 9) {
                    intStack[intStackPointer++] = 1;
                    return;
                }
                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == PLAYERMODLEVEL) {
                if (Static38.playerModLevel >= 5 && Static38.playerModLevel <= 9) {
                    intStack[intStackPointer++] = Static38.playerModLevel;
                    return;
                }
                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == PLAYERMEMBER) {
                intStack[intStackPointer++] = Client.isMember ? 1 : 0;
                return;
            }

            if (op == COMLEVEL_ACTIVE) {
                intStack[intStackPointer++] = PlayerEntity.self.combatLevel;
                return;
            }

            if (op == GENDER) {
                intStack[intStackPointer++] = PlayerEntity.self.playerModel != null && PlayerEntity.self.playerModel.female ? 1 : 0;
                return;
            }

            if (op == MAP_QUICKCHAT) {
                intStack[intStackPointer++] = Static617.quickChatWorld ? 1 : 0;
                return;
            }

            if (op == INV_FREESPACE) {
                @Pc(15) int local15 = intStack[--intStackPointer];
                intStack[intStackPointer++] = ClientInventory.freeSpace(local15);
                return;
            }

            if (op == INV_TOTALPARAM) {
                intStackPointer -= 2;

                @Pc(15) int id = intStack[intStackPointer];
                @Pc(21) int param = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = ClientInventory.totalParam(id, param, false);
                return;
            }

            if (op == INV_TOTALPARAM_STACK) {
                intStackPointer -= 2;

                @Pc(15) int id = intStack[intStackPointer];
                @Pc(21) int param = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = ClientInventory.totalParam(id, param, true);
                return;
            }

            if (op == 3333) {
                intStack[intStackPointer++] = Static696.method9034();
                return;
            }

            if (op == MAP_LANG) {
                intStack[intStackPointer++] = Client.language;
                return;
            }

            if (op == MOVE_COORD) {
                intStackPointer -= 4;

                @Pc(15) int coord = intStack[intStackPointer];
                @Pc(21) int x = intStack[intStackPointer + 1];
                @Pc(27) int y = intStack[intStackPointer + 2];
                @Pc(506) int z = intStack[intStackPointer + 3];

                coord += x << 14;
                coord += y << 28;
                coord += z;

                intStack[intStackPointer++] = coord;
                return;
            }

            if (op == AFFILIATE) {
                intStack[intStackPointer++] = Client.affid;
                return;
            }

            if (op == PROFILE_CPU) {
                intStack[intStackPointer++] = Static65.profileCpu();
                return;
            }

            if (op == PLAYERDEMO) {
                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == APPLET_HASFOCUS) {
                intStack[intStackPointer++] = GameShell.focus ? 1 : 0;
                return;
            }

            if (op == FROMBILLING) {
                intStack[intStackPointer++] = Client.fromBilling ? 1 : 0;
                return;
            }

            if (op == GET_MOUSEX) {
                intStack[intStackPointer++] = MouseMonitor.instance.getRecordedX();
                return;
            }

            if (op == GET_MOUSEY) {
                intStack[intStackPointer++] = MouseMonitor.instance.getRecordedY();
                return;
            }

            if (op == GET_ACTIVE_MINIMENU_ENTRY) {
                stringStack[stringStackPointer++] = MiniMenu.activeEntry();
                return;
            }

            if (op == GET_SECOND_MINIMENU_ENTRY) {
                stringStack[stringStackPointer++] = MiniMenu.secondEntry();
                return;
            }

            if (op == GET_MINIMENU_LENGTH) {
                intStack[intStackPointer++] = MiniMenu.length();
                return;
            }

            if (op == GET_CURRENTCURSOR) {
                intStack[intStackPointer++] = Static470.currentCursor;
                return;
            }

            if (op == GET_SELFYANGLE) {
                intStack[intStackPointer++] = PlayerEntity.self.yaw.getValue(16383) >> 3;
                return;
            }

            if (op == MAP_ISOWNER) {
                @Pc(4911) String name = stringStack[--stringStackPointer];

                if (Static416.mapOwner != null && Static416.mapOwner.equalsIgnoreCase(name)) {
                    intStack[intStackPointer++] = 1;
                    return;
                }

                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == GET_MOUSEBUTTONS) {
                intStack[intStackPointer++] = MouseMonitor.instance.isLeftDown() ? 1 : 0;
                intStack[intStackPointer++] = MouseMonitor.instance.isMiddleDown() ? 1 : 0;
                intStack[intStackPointer++] = MouseMonitor.instance.isRightDown() ? 1 : 0;
                return;
            }
        } else if (op < 3500) {
            if (op == ENUM_STRING) {
                intStackPointer -= 2;

                @Pc(15) int id = intStack[intStackPointer];
                @Pc(21) int key = intStack[intStackPointer + 1];
                @Pc(6822) EnumType enumType = EnumTypeList.instance.list(id);

                stringStack[stringStackPointer++] = enumType.getString(key);
                return;
            }

            if (op == ENUM) {
                intStackPointer -= 4;

                @Pc(15) int keyType = intStack[intStackPointer];
                @Pc(21) int valType = intStack[intStackPointer + 1];
                @Pc(27) int id = intStack[intStackPointer + 2];
                @Pc(506) int key = intStack[intStackPointer + 3];
                @Pc(6868) EnumType enumType = EnumTypeList.instance.list(id);

                if (enumType.keyType == keyType && enumType.valType == valType) {
                    if (valType == 's') {
                        stringStack[stringStackPointer++] = enumType.getString(key);
                        return;
                    }

                    intStack[intStackPointer++] = enumType.getInt(key);
                    return;
                }

                throw new RuntimeException("C3408-1 " + id + "-" + key);
            }

            if (op == ENUM_HASOUTPUT) {
                intStackPointer -= 3;

                @Pc(15) int valType = intStack[intStackPointer];
                @Pc(21) int id = intStack[intStackPointer + 1];
                @Pc(27) int key = intStack[intStackPointer + 2];
                if (id == -1) {
                    throw new RuntimeException("C3409-2");
                }

                @Pc(6963) EnumType enumType = EnumTypeList.instance.list(id);
                if (enumType.valType != valType) {
                    throw new RuntimeException("C3409-1");
                }

                intStack[intStackPointer++] = enumType.hasOutput(key) ? 1 : 0;
                return;
            }

            if (op == ENUM_HASOUTPUT_STRING) {
                @Pc(15) int id = intStack[--intStackPointer];
                @Pc(1394) String key = stringStack[--stringStackPointer];
                if (id == -1) {
                    throw new RuntimeException("C3410-2");
                }

                @Pc(6822) EnumType enumType = EnumTypeList.instance.list(id);
                if (enumType.valType != 's') {
                    throw new RuntimeException("C3410-1");
                }

                intStack[intStackPointer++] = enumType.hasOutputString(key) ? 1 : 0;
                return;
            }

            if (op == ENUM_GETOUTPUT_COUNT) {
                @Pc(15) int id = intStack[--intStackPointer];
                @Pc(7072) EnumType enumType = EnumTypeList.instance.list(id);
                intStack[intStackPointer++] = enumType.getOutputCount();
                return;
            }

            if (op == ENUM_GETREVERSECOUNT) {
                intStackPointer -= 3;

                @Pc(15) int valType = intStack[intStackPointer];
                @Pc(21) int id = intStack[intStackPointer + 1];
                @Pc(27) int key = intStack[intStackPointer + 2];
                if (id == -1) {
                    throw new RuntimeException();
                }

                @Pc(6963) EnumType enumMapping = EnumTypeList.instance.list(id);
                if (enumMapping.valType != valType) {
                    throw new RuntimeException();
                }

                @Pc(7133) EnumMapping mapping = enumMapping.getReversed(key);
                @Pc(72) int count = 0;
                if (mapping != null) {
                    count = mapping.index.length;
                }

                intStack[intStackPointer++] = count;
                return;
            }

            if (op == ENUM_GETREVERSECOUNT_STRING) {
                @Pc(15) int id = intStack[--intStackPointer];
                @Pc(1394) String key = stringStack[--stringStackPointer];
                if (id == -1) {
                    throw new RuntimeException();
                }

                @Pc(6822) EnumType enumType = EnumTypeList.instance.list(id);
                if (enumType.valType != 's') {
                    throw new RuntimeException();
                }

                @Pc(7196) EnumStringMapping mapping = enumType.getReversed(key);
                @Pc(2978) int count = 0;
                if (mapping != null) {
                    count = mapping.index.length;
                }

                intStack[intStackPointer++] = count;
                return;
            }

            if (op == ENUM_GETREVERSEINDEX) {
                intStackPointer -= 5;

                @Pc(15) int valType = intStack[intStackPointer];
                @Pc(21) int keyType = intStack[intStackPointer + 1];
                @Pc(27) int id = intStack[intStackPointer + 2];
                @Pc(506) int key = intStack[intStackPointer + 3];
                @Pc(2978) int index = intStack[intStackPointer + 4];
                if (id == -1) {
                    throw new RuntimeException();
                }

                @Pc(7261) EnumType enumType = EnumTypeList.instance.list(id);
                if (enumType.keyType != keyType) {
                    throw new RuntimeException();
                }
                if (enumType.valType != valType) {
                    throw new RuntimeException();
                }

                @Pc(7284) EnumMapping mapping = enumType.getReversed(key);
                if (index >= 0 && mapping != null && mapping.index.length > index) {
                    intStack[intStackPointer++] = mapping.index[index];
                    return;
                }

                throw new RuntimeException();
            }

            if (op == ENUM_GETREVERSEINDEX_STRING) {
                intStackPointer -= 3;

                @Pc(15) int keyType = intStack[intStackPointer];
                @Pc(21) int id = intStack[intStackPointer + 1];
                @Pc(27) int local27 = intStack[intStackPointer + 2];
                @Pc(7345) String key = stringStack[--stringStackPointer];
                if (id == -1) {
                    throw new RuntimeException();
                }

                @Pc(6868) EnumType enumType = EnumTypeList.instance.list(id);
                if (enumType.keyType != keyType) {
                    throw new RuntimeException();
                }
                if (enumType.valType != 's') {
                    throw new RuntimeException();
                }

                @Pc(7381) EnumStringMapping mapping = enumType.getReversed(key);
                if (local27 >= 0 && mapping != null && mapping.index.length > local27) {
                    intStack[intStackPointer++] = mapping.index[local27];
                    return;
                }

                throw new RuntimeException();
            }
        } else if (op < 3700) {
            if (op == FRIEND_COUNT) {
                if (FriendsList.status == 0) {
                    intStack[intStackPointer++] = -2;
                    return;
                }

                if (FriendsList.status == 1) {
                    intStack[intStackPointer++] = -1;
                    return;
                }

                intStack[intStackPointer++] = FriendsList.count;
                return;
            }

            if (op == FRIEND_GETNAME) {
                @Pc(15) int index = intStack[--intStackPointer];

                if (FriendsList.status == 2 && index < FriendsList.count) {
                    stringStack[stringStackPointer++] = FriendsList.names[index];

                    if (FriendsList.formerNames[index] != null) {
                        stringStack[stringStackPointer++] = FriendsList.formerNames[index];
                        return;
                    }

                    stringStack[stringStackPointer++] = "";
                    return;
                }

                stringStack[stringStackPointer++] = "";
                stringStack[stringStackPointer++] = "";
                return;
            }

            if (op == FRIEND_GETWORLD) {
                @Pc(15) int index = intStack[--intStackPointer];

                if (FriendsList.status == 2 && index < FriendsList.count) {
                    intStack[intStackPointer++] = FriendsList.worlds[index];
                    return;
                }

                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == FRIEND_GETRANK) {
                @Pc(15) int index = intStack[--intStackPointer];

                if (FriendsList.status == 2 && index < FriendsList.count) {
                    intStack[intStackPointer++] = FriendsList.ranks[index];
                    return;
                }

                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == FRIEND_SETRANK) {
                @Pc(4911) String name = stringStack[--stringStackPointer];
                @Pc(21) int rank = intStack[--intStackPointer];
                FriendsList.setRank(name, rank);
                return;
            }

            if (op == FRIEND_ADD) {
                @Pc(4911) String name = stringStack[--stringStackPointer];
                FriendsList.add(name);
                return;
            }

            if (op == FRIEND_DEL) {
                @Pc(4911) String name = stringStack[--stringStackPointer];
                FriendsList.delete(name);
                return;
            }

            if (op == IGNORE_ADD) {
                @Pc(4911) String name = stringStack[--stringStackPointer];
                IgnoreList.add(name, false);
                return;
            }

            if (op == IGNORE_DEL) {
                @Pc(4911) String name = stringStack[--stringStackPointer];
                IgnoreList.delete(name);
                return;
            }

            if (op == FRIEND_TEST) {
                @Pc(4911) String name = stringStack[--stringStackPointer];

                if (name.startsWith("<img=0>") || name.startsWith("<img=1>")) {
                    name = name.substring(7);
                }

                intStack[intStackPointer++] = FriendsList.contains(0, name) ? 1 : 0;
                return;
            }

            if (op == FRIEND_GETWORLDNAME) {
                @Pc(15) int index = intStack[--intStackPointer];

                if (FriendsList.status == 2 && index < FriendsList.count) {
                    stringStack[stringStackPointer++] = FriendsList.worldNames[index];
                    return;
                }

                stringStack[stringStackPointer++] = "";
                return;
            }

            if (op == CLAN_GETCHATDISPLAYNAME) {
                if (FriendChat.name != null) {
                    stringStack[stringStackPointer++] = NameTools.normalise(FriendChat.name);
                    return;
                }
                stringStack[stringStackPointer++] = "";
                return;
            }

            if (op == CLAN_GETCHATCOUNT) {
                if (FriendChat.name != null) {
                    intStack[intStackPointer++] = FriendChat.count;
                    return;
                }
                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == CLAN_GETCHATUSERNAME) {
                @Pc(15) int index = intStack[--intStackPointer];

                if (FriendChat.name != null && index < FriendChat.count) {
                    stringStack[stringStackPointer++] = FriendChat.users[index].name;
                    return;
                }

                stringStack[stringStackPointer++] = "";
                return;
            }

            if (op == CLAN_GETCHATUSERWORLD) {
                @Pc(15) int index = intStack[--intStackPointer];

                if (FriendChat.name != null && index < FriendChat.count) {
                    intStack[intStackPointer++] = FriendChat.users[index].world;
                    return;
                }

                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == CLAN_GETCHATUSERRANK) {
                @Pc(15) int index = intStack[--intStackPointer];

                if (FriendChat.name != null && index < FriendChat.count) {
                    intStack[intStackPointer++] = FriendChat.users[index].rank;
                    return;
                }

                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == CLAN_GETCHATMINKICK) {
                intStack[intStackPointer++] = FriendChat.kickRank;
                return;
            }

            if (op == CLAN_KICKUSER) {
                @Pc(4911) String name = stringStack[--stringStackPointer];
                FriendChat.kick(name);
                return;
            }

            if (op == CLAN_GETCHATRANK) {
                intStack[intStackPointer++] = FriendChat.rank;
                return;
            }

            if (op == CLAN_JOINCHAT) {
                @Pc(4911) String name = stringStack[--stringStackPointer];
                FriendChat.join(name);
                return;
            }

            if (op == CLAN_LEAVECHAT) {
                FriendChat.leave();
                return;
            }

            if (op == IGNORE_COUNT) {
                if (FriendsList.status == 0) {
                    intStack[intStackPointer++] = -1;
                    return;
                }

                intStack[intStackPointer++] = IgnoreList.count;
                return;
            }

            if (op == IGNORE_GETNAME) {
                @Pc(15) int index = intStack[--intStackPointer];

                if (FriendsList.status != 0 && index < IgnoreList.count) {
                    stringStack[stringStackPointer++] = IgnoreList.names[index];

                    if (IgnoreList.formerNames[index] != null) {
                        stringStack[stringStackPointer++] = IgnoreList.formerNames[index];
                        return;
                    }

                    stringStack[stringStackPointer++] = "";
                    return;
                }

                stringStack[stringStackPointer++] = "";
                stringStack[stringStackPointer++] = "";
                return;
            }

            if (op == IGNORE_TEXT) {
                @Pc(4911) String name = stringStack[--stringStackPointer];
                if (name.startsWith("<img=0>") || name.startsWith("<img=1>")) {
                    name = name.substring(7);
                }

                intStack[intStackPointer++] = IgnoreList.contains(name) ? 1 : 0;
                return;
            }

            if (op == CLAN_ISSELF) {
                @Pc(15) int index = intStack[--intStackPointer];

                if (FriendChat.users != null && index < FriendChat.count && FriendChat.users[index].usernameUnfiltered.equalsIgnoreCase(PlayerEntity.self.nameUnfiltered)) {
                    intStack[intStackPointer++] = 1;
                    return;
                }

                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == CLAN_GETCHATOWNERNAME) {
                if (FriendChat.owner != null) {
                    stringStack[stringStackPointer++] = FriendChat.owner;
                    return;
                }

                stringStack[stringStackPointer++] = "";
                return;
            }

            if (op == CLAN_GETCHATUSERWORLDNAME) {
                @Pc(15) int index = intStack[--intStackPointer];

                if (FriendChat.name != null && index < FriendChat.count) {
                    stringStack[stringStackPointer++] = FriendChat.users[index].worldName;
                    return;
                }

                stringStack[stringStackPointer++] = "";
                return;
            }

            if (op == FRIEND_SAME_GAME) {
                @Pc(15) int index = intStack[--intStackPointer];

                if (FriendsList.status == 2 && index >= 0 && index < FriendsList.count) {
                    intStack[intStackPointer++] = FriendsList.sameGameFlags[index] ? 1 : 0;
                    return;
                }

                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == FRIEND_GETSLOTFROMNAME) {
                @Pc(4911) String name = stringStack[--stringStackPointer];
                if (name.startsWith("<img=0>") || name.startsWith("<img=1>")) {
                    name = name.substring(7);
                }

                intStack[intStackPointer++] = Static664.method8658(name);
                return;
            }

            if (op == PLAYERCOUNTRY) {
                intStack[intStackPointer++] = Client.country;
                return;
            }

            if (op == IGNORE_ADD_TEMP) {
                @Pc(4911) String name = stringStack[--stringStackPointer];
                IgnoreList.add(name, true);
                return;
            }

            if (op == IGNORE_IS_TEMP) {
                @Pc(15) int index = intStack[--intStackPointer];
                intStack[intStackPointer++] = IgnoreList.temporary[index] ? 1 : 0;
                return;
            }

            if (op == CLAN_GETCHATUSERNAME_UNFILTERED) {
                @Pc(15) int index = intStack[--intStackPointer];
                if (FriendChat.name != null && index < FriendChat.count) {
                    stringStack[stringStackPointer++] = FriendChat.users[index].usernameUnfiltered;
                    return;
                }

                stringStack[stringStackPointer++] = "";
                return;
            }

            if (op == IGNORE_GETNAME_UNFILTERED) {
                @Pc(15) int index = intStack[--intStackPointer];
                if (FriendsList.status != 0 && index < IgnoreList.count) {
                    stringStack[stringStackPointer++] = IgnoreList.namesUnfiltered[index];
                    return;
                }

                stringStack[stringStackPointer++] = "";
                return;
            }

            if (op == FRIEND_IS_REFERRED) {
                @Pc(15) int index = intStack[--intStackPointer];
                if (FriendsList.status == 2 && index < FriendsList.count) {
                    intStack[intStackPointer++] = FriendsList.referredFlags[index] ? 1 : 0;
                    return;
                }

                intStack[intStackPointer++] = 0;
                return;
            }
        } else if (op < 3800) {
            if (op == ACTIVELCANSETTINGS_FIND_LISTENED) {
                if (ClanSettings.listened != null) {
                    intStack[intStackPointer++] = 1;
                    activeClanSettings = ClanSettings.listened;
                    return;
                }

                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == ACTIVECLANSETTINGS_FIND_AFFINED) {
                if (ClanSettings.affined != null) {
                    intStack[intStackPointer++] = 1;
                    activeClanSettings = ClanSettings.affined;
                    return;
                }

                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == ACTIVECLANSETTINGS_GETCLANNAME) {
                stringStack[stringStackPointer++] = activeClanSettings.name;
                return;
            }

            if (op == ACTIVECLANSETTINGS_GETALLOWUNAFFINED) {
                intStack[intStackPointer++] = activeClanSettings.allowNonMembers ? 1 : 0;
                return;
            }

            if (op == ACTIVECLANSETTINGS_GETRANKTALK) {
                intStack[intStackPointer++] = activeClanSettings.rankTalk;
                return;
            }

            if (op == ACTIVECLANSETTINGS_GETRANKKICK) {
                intStack[intStackPointer++] = activeClanSettings.rankKick;
                return;
            }

            if (op == ACTIVECLANSETTINGS_GETRANKLOOTSHARE) {
                intStack[intStackPointer++] = activeClanSettings.rankLootShare;
                return;
            }

            if (op == ACTIVECLANSETTINGS_GETCOINSHARE) {
                intStack[intStackPointer++] = activeClanSettings.coinshare;
                return;
            }

            if (op == ACTIVECLANSETTINGS_GETAFFINEDCOUNT) {
                intStack[intStackPointer++] = activeClanSettings.affinedCount;
                return;
            }

            if (op == ACTIVECLANSETTINGS_GETAFFINEDDISPLAYNAME) {
                @Pc(15) int index = intStack[--intStackPointer];
                stringStack[stringStackPointer++] = activeClanSettings.affinedDisplayNames[index];
                return;
            }

            if (op == ACTIVECLANSETTINGS_GETAFFINEDRANK) {
                @Pc(15) int local15 = intStack[--intStackPointer];
                intStack[intStackPointer++] = activeClanSettings.affinedRanks[local15];
                return;
            }

            if (op == ACTIVECLANSETTINGS_GETBANNEDCOUNT) {
                intStack[intStackPointer++] = activeClanSettings.bannedCount;
                return;
            }

            if (op == ACTIVECLANSETTINGS_GETBANNEDDISPLAYNAME) {
                @Pc(15) int index = intStack[--intStackPointer];
                stringStack[stringStackPointer++] = activeClanSettings.bannedDisplayNames[index];
                return;
            }

            if (op == ACTIVECLANSETTINGS_GETAFFINEDEXTRAINFO) {
                intStackPointer -= 3;
                @Pc(15) int id = intStack[intStackPointer];
                @Pc(21) int startBit = intStack[intStackPointer + 1];
                @Pc(27) int endBit = intStack[intStackPointer + 2];
                intStack[intStackPointer++] = activeClanSettings.getAffinedExtraInfo(id, startBit, endBit);
                return;
            }

            if (op == ACTIVECLANSETTINGS_GETCURRENTOWNER_SLOT) {
                intStack[intStackPointer++] = activeClanSettings.currentOwnerSlot;
                return;
            }

            if (op == ACTIVECLANSETTINGS_GETREPLACEMENTOWNER_SLOT) {
                intStack[intStackPointer++] = activeClanSettings.replacementOwnerSlot;
                return;
            }

            if (op == ACTIVECLANSETTINGS_GETAFFINEDSLOT) {
                intStack[intStackPointer++] = activeClanSettings.affinedSlot(stringStack[--stringStackPointer]);
                return;
            }

            if (op == ACTIVECLANSETTINGS_GETSORTEDAFFINEDSLOT) {
                intStack[intStackPointer - 1] = activeClanSettings.sortedAffinedSlots()[intStack[intStackPointer - 1]];
                return;
            }

            if (op == AFFINEDCLANSETTINGS_ADDBANNED_FROMCHANNEL) {
                Static180.ban(intStack[--intStackPointer]);
                return;
            }

            if (op == ACTIVECLANSETTINGS_GETAFFINEDJOINRUNEDAY) {
                @Pc(15) int index = intStack[--intStackPointer];
                intStack[intStackPointer++] = activeClanSettings.affinedJoinRuneday[index];
                return;
            }

            if (op == ACTIVECLANCHANNEL_FIND_LISTENED) {
                if (ClanChannel.listened != null) {
                    intStack[intStackPointer++] = 1;
                    activeClanChannel = ClanChannel.listened;
                    return;
                }

                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == ACTIVECLANCHANNEL_FIND_AFFINED) {
                if (ClanChannel.affined != null) {
                    intStack[intStackPointer++] = 1;
                    activeClanChannel = ClanChannel.affined;
                    return;
                }

                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == ACTIVECLANCHANNEL_GETCLANNAME) {
                stringStack[stringStackPointer++] = activeClanChannel.clanName;
                return;
            }

            if (op == ACTIVECLANCHANNEL_GETRANKKICK) {
                intStack[intStackPointer++] = activeClanChannel.kickRank;
                return;
            }

            if (op == ACTIVECLANCHANNEL_GETRANKTALK) {
                intStack[intStackPointer++] = activeClanChannel.talkRank;
                return;
            }

            if (op == ACTIVECLANCHANNEL_GETUSERCOUNT) {
                intStack[intStackPointer++] = activeClanChannel.userCount;
                return;
            }

            if (op == ACTIVECLANCHANNEL_GETUSERDISPLAYNAME) {
                @Pc(15) int index = intStack[--intStackPointer];
                stringStack[stringStackPointer++] = activeClanChannel.users[index].displayName;
                return;
            }

            if (op == ACTIVECLANCHANNEL_GETUSERRANK) {
                @Pc(15) int index = intStack[--intStackPointer];
                intStack[intStackPointer++] = activeClanChannel.users[index].rank;
                return;
            }

            if (op == ACTIVECLANCHANNEL_GETUSERWORLD) {
                @Pc(15) int index = intStack[--intStackPointer];
                intStack[intStackPointer++] = activeClanChannel.users[index].world;
                return;
            }

            if (op == ACTIVECLANCHANNEL_KICKUSER) {
                @Pc(15) int index = intStack[--intStackPointer];
                Static525.kick(index, activeClanChannel == ClanChannel.affined);
                return;
            }

            if (op == ACTIVECLANCHANNEL_GETUSERSLOT) {
                intStack[intStackPointer++] = activeClanChannel.userSlot(stringStack[--stringStackPointer]);
                return;
            }

            if (op == ACTIVECLANCHANNEL_GETSORTEDUSERSLOT) {
                intStack[intStackPointer - 1] = activeClanChannel.sortedUserSlots()[intStack[intStackPointer - 1]];
                return;
            }

            if (op == CLANPROFILE_FIND) {
                intStack[intStackPointer++] = Static279.clanVars == null ? 0 : 1;
                return;
            }
        } else if (op < 4000) {
            if (op == STOCKMARKET_GETOFFERTYPE) {
                @Pc(15) int index = intStack[--intStackPointer];
                intStack[intStackPointer++] = StockmarketManager.offers[index].getOfferType();
                return;
            }

            if (op == STOCKMARKET_GETOFFERITEM) {
                @Pc(15) int index = intStack[--intStackPointer];
                intStack[intStackPointer++] = StockmarketManager.offers[index].objId;
                return;
            }

            if (op == STOCKMARKET_GETOFFERPRICE) {
                @Pc(15) int index = intStack[--intStackPointer];
                intStack[intStackPointer++] = StockmarketManager.offers[index].price;
                return;
            }

            if (op == STOCKMARKET_GETOFFERCOUNT) {
                @Pc(15) int index = intStack[--intStackPointer];
                intStack[intStackPointer++] = StockmarketManager.offers[index].count;
                return;
            }

            if (op == STOCKMARKET_GETOFFERCOMPLETEDCOUNT) {
                @Pc(15) int index = intStack[--intStackPointer];
                intStack[intStackPointer++] = StockmarketManager.offers[index].completedCount;
                return;
            }

            if (op == STOCKMARKET_GETOFFERCOMPLETEDGOLD) {
                @Pc(15) int index = intStack[--intStackPointer];
                intStack[intStackPointer++] = StockmarketManager.offers[index].completedGold;
                return;
            }

            if (op == STOCKMARKET_ISOFFEREMPTY) {
                @Pc(15) int index = intStack[--intStackPointer];
                @Pc(21) int status = StockmarketManager.offers[index].getStatus();
                intStack[intStackPointer++] = status == 0 ? 1 : 0;
                return;
            }

            if (op == STOCKMARKET_ISOFFERSTABLE) {
                @Pc(15) int index = intStack[--intStackPointer];
                @Pc(21) int status = StockmarketManager.offers[index].getStatus();
                intStack[intStackPointer++] = status == 2 ? 1 : 0;
                return;
            }

            if (op == STOCKMARKET_ISOFFERFINISHED) {
                @Pc(15) int index = intStack[--intStackPointer];
                @Pc(21) int status = StockmarketManager.offers[index].getStatus();
                intStack[intStackPointer++] = status == 5 ? 1 : 0;
                return;
            }

            if (op == STOCKMARKET_ISOFFERADDING) {
                @Pc(15) int index = intStack[--intStackPointer];
                @Pc(21) int status = StockmarketManager.offers[index].getStatus();
                intStack[intStackPointer++] = status == 1 ? 1 : 0;
                return;
            }
        } else if (op < 4100) {
            if (op == ADD) {
                intStackPointer -= 2;

                @Pc(15) int a = intStack[intStackPointer];
                @Pc(21) int b = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = a + b;
                return;
            }

            if (op == SUB) {
                intStackPointer -= 2;

                @Pc(15) int a = intStack[intStackPointer];
                @Pc(21) int b = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = a - b;
                return;
            }

            if (op == MULTIPLY) {
                intStackPointer -= 2;

                @Pc(15) int a = intStack[intStackPointer];
                @Pc(21) int b = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = a * b;
                return;
            }

            if (op == DIVIDE) {
                intStackPointer -= 2;

                @Pc(15) int a = intStack[intStackPointer];
                @Pc(21) int b = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = a / b;
                return;
            }

            if (op == RANDOM) {
                @Pc(15) int v = intStack[--intStackPointer];
                intStack[intStackPointer++] = (int) (Math.random() * (double) v);
                return;
            }

            if (op == RANDOMINC) {
                @Pc(15) int v = intStack[--intStackPointer];
                intStack[intStackPointer++] = (int) (Math.random() * (double) (v + 1));
                return;
            }

            if (op == INTERPOLATE) {
                intStackPointer -= 5;

                @Pc(15) int y0 = intStack[intStackPointer];
                @Pc(21) int y1 = intStack[intStackPointer + 1];
                @Pc(27) int x0 = intStack[intStackPointer + 2];
                @Pc(506) int x1 = intStack[intStackPointer + 3];
                @Pc(2978) int x2 = intStack[intStackPointer + 4];

                intStack[intStackPointer++] = y0 + (y1 - y0) * (x2 - x0) / (x1 - x0);
                return;
            }

            if (op == ADDPERCENT) {
                intStackPointer -= 2;

                @Pc(9705) long a = intStack[intStackPointer];
                @Pc(9712) long b = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = (int) (a + ((a * b) / 100L));
                return;
            }

            if (op == SETBIT) {
                intStackPointer -= 2;

                @Pc(15) int v = intStack[intStackPointer];
                @Pc(21) int bit = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = v | (0x1 << bit);
                return;
            }

            if (op == CLEARBIT) {
                intStackPointer -= 2;

                @Pc(15) int v = intStack[intStackPointer];
                @Pc(21) int bit = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = v & -(0x1 << bit) - 1;
                return;
            }

            if (op == TESTBIT) {
                intStackPointer -= 2;

                @Pc(15) int v = intStack[intStackPointer];
                @Pc(21) int bit = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = (v & 0x1 << bit) == 0 ? 0 : 1;
                return;
            }

            if (op == MODULO) {
                intStackPointer -= 2;

                @Pc(15) int local15 = intStack[intStackPointer];
                @Pc(21) int local21 = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = local15 % local21;
                return;
            }

            if (op == POW) {
                intStackPointer -= 2;

                @Pc(15) int a = intStack[intStackPointer];
                @Pc(21) int b = intStack[intStackPointer + 1];

                if (a == 0) {
                    intStack[intStackPointer++] = 0;
                    return;
                }

                intStack[intStackPointer++] = (int) Math.pow(a, b);
                return;
            }

            if (op == INVPOW) {
                intStackPointer -= 2;

                @Pc(15) int a = intStack[intStackPointer];
                @Pc(21) int b = intStack[intStackPointer + 1];

                if (a == 0) {
                    intStack[intStackPointer++] = 0;
                    return;
                }

                if (b == 0) {
                    intStack[intStackPointer++] = Integer.MAX_VALUE;
                    return;
                }

                intStack[intStackPointer++] = (int) Math.pow(a, 1.0D / (double) b);
                return;
            }

            if (op == AND) {
                intStackPointer -= 2;

                @Pc(15) int a = intStack[intStackPointer];
                @Pc(21) int b = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = a & b;
                return;
            }

            if (op == OR) {
                intStackPointer -= 2;

                @Pc(15) int a = intStack[intStackPointer];
                @Pc(21) int b = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = a | b;
                return;
            }

            if (op == MIN) {
                intStackPointer -= 2;

                @Pc(15) int a = intStack[intStackPointer];
                @Pc(21) int b = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = a < b ? a : b;
                return;
            }

            if (op == MAX) {
                intStackPointer -= 2;

                @Pc(15) int a = intStack[intStackPointer];
                @Pc(21) int b = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = a > b ? a : b;
                return;
            }

            if (op == SCALE) {
                intStackPointer -= 3;

                @Pc(9705) long a = intStack[intStackPointer];
                @Pc(9712) long denominator = intStack[intStackPointer + 1];
                @Pc(10099) long numerator = intStack[intStackPointer + 2];

                intStack[intStackPointer++] = (int) ((a * numerator) / denominator);
                return;
            }

            if (op == RANDOM_SOUND_PITCH) {
                intStackPointer -= 2;

                @Pc(15) int min = intStack[intStackPointer];
                @Pc(21) int max = intStack[intStackPointer + 1];
                if (min > 700 || max > 700) {
                    intStack[intStackPointer++] = 256;
                }

                @Pc(10162) double local10162 = (Math.random() * (double) (max + min) + 800.0D - (double) min) / 100.0D;
                intStack[intStackPointer++] = (int) (Math.pow(2.0D, local10162) + 0.5D);
                return;
            }

            if (op == HSVTORGB) {
                @Pc(15) int local15 = intStack[--intStackPointer];
                intStack[intStackPointer++] = ColourUtils.HSV_TO_RGB[ColourUtils.hslToHsv(local15) & 0xFFFF];
                return;
            }
        } else if (op < 4200) {
            if (op == APPEND_NUM) {
                @Pc(4911) String text = stringStack[--stringStackPointer];
                @Pc(21) int number = intStack[--intStackPointer];
                stringStack[stringStackPointer++] = text + number;
                return;
            }

            if (op == APPEND) {
                stringStackPointer -= 2;
                @Pc(4911) String a = stringStack[stringStackPointer];
                @Pc(1394) String b = stringStack[stringStackPointer + 1];
                stringStack[stringStackPointer++] = a + b;
                return;
            }

            if (op == APPEND_SIGNNUM) {
                @Pc(4911) String text = stringStack[--stringStackPointer];
                @Pc(21) int num = intStack[--intStackPointer];
                stringStack[stringStackPointer++] = text + StringTools.decimalWithSign(true, num);
                return;
            }

            if (op == LOWERCASE) {
                @Pc(4911) String text = stringStack[--stringStackPointer];
                stringStack[stringStackPointer++] = text.toLowerCase();
                return;
            }

            if (op == FROMDATE) {
                stringStack[stringStackPointer++] = TimeUtils.dateFromTime(TimeUtils.timeFromRuneday(intStack[--intStackPointer]), Client.language);
                return;
            }

            if (op == TEXT_GENDER) {
                stringStackPointer -= 2;

                @Pc(4911) String male = stringStack[stringStackPointer];
                @Pc(1394) String female = stringStack[stringStackPointer + 1];
                if (PlayerEntity.self.playerModel != null && PlayerEntity.self.playerModel.female) {
                    stringStack[stringStackPointer++] = female;
                    return;
                }

                stringStack[stringStackPointer++] = male;
                return;
            }

            if (op == TOSTRING) {
                @Pc(15) int i = intStack[--intStackPointer];
                stringStack[stringStackPointer++] = Integer.toString(i);
                return;
            }

            if (op == COMPARE) {
                stringStackPointer -= 2;
                intStack[intStackPointer++] = Static540.compare(stringStack[stringStackPointer + 1], Client.language, stringStack[stringStackPointer]);
                return;
            }

            if (op == PARAHEIGHT) {
                @Pc(4911) String text = stringStack[--stringStackPointer];
                intStackPointer -= 2;

                @Pc(21) int lineWidth = intStack[intStackPointer];
                @Pc(27) int font = intStack[intStackPointer + 1];
                @Pc(10482) FontMetrics metrics = FontMetrics.loadGroup(js5.FONTMETRICS, font);

                intStack[intStackPointer++] = metrics.paraHeight(text, Sprites.nameIcons, lineWidth);
                return;
            }

            if (op == PARAWIDTH) {
                @Pc(4911) String text = stringStack[--stringStackPointer];
                intStackPointer -= 2;

                @Pc(21) int lineWidth = intStack[intStackPointer];
                @Pc(27) int font = intStack[intStackPointer + 1];
                @Pc(10482) FontMetrics metrics = FontMetrics.loadGroup(js5.FONTMETRICS, font);

                intStack[intStackPointer++] = metrics.paraWidth(Sprites.nameIcons, text, lineWidth);
                return;
            }

            if (op == TEXT_SWITCH) {
                stringStackPointer -= 2;

                @Pc(4911) String a = stringStack[stringStackPointer];
                @Pc(1394) String b = stringStack[stringStackPointer + 1];
                if (intStack[--intStackPointer] == 1) {
                    stringStack[stringStackPointer++] = a;
                    return;
                }

                stringStack[stringStackPointer++] = b;
                return;
            }

            if (op == ESCAPE) {
                @Pc(4911) String text = stringStack[--stringStackPointer];
                stringStack[stringStackPointer++] = StringTools.escapeBrackets(text);
                return;
            }

            if (op == APPEND_CHAR) {
                @Pc(4911) String text = stringStack[--stringStackPointer];
                @Pc(21) int c = intStack[--intStackPointer];
                if (c == -1) {
                    throw new RuntimeException("null char");
                }

                stringStack[stringStackPointer++] = text + (char) c;
                return;
            }

            if (op == CHAR_ISPRINTABLE) {
                @Pc(15) int c = intStack[--intStackPointer];
                intStack[intStackPointer++] = charIsPrintable((char) c);
                return;
            }

            if (op == CHAR_ISALPHANUMERIC) {
                @Pc(15) int c = intStack[--intStackPointer];
                intStack[intStackPointer++] = StringTools.isAlphanumeric((char) c) ? 1 : 0;
                return;
            }

            if (op == CHAR_ISALPHA) {
                @Pc(15) int c = intStack[--intStackPointer];
                intStack[intStackPointer++] = StringTools.isAlphabetical((char) c) ? 1 : 0;
                return;
            }

            if (op == CHAR_ISNUMERIC) {
                @Pc(15) int c = intStack[--intStackPointer];
                intStack[intStackPointer++] = StringTools.isNumeric((char) c) ? 1 : 0;
                return;
            }

            if (op == STRING_LENGTH) {
                @Pc(4911) String text = stringStack[--stringStackPointer];

                if (text != null) {
                    intStack[intStackPointer++] = text.length();
                    return;
                }

                intStack[intStackPointer++] = 0;
                return;
            }

            if (op == SUBSTRING) {
                @Pc(4911) String text = stringStack[--stringStackPointer];
                intStackPointer -= 2;

                @Pc(21) int beginIndex = intStack[intStackPointer];
                @Pc(27) int endIndex = intStack[intStackPointer + 1];

                stringStack[stringStackPointer++] = text.substring(beginIndex, endIndex);
                return;
            }

            if (op == REMOVETAGS) {
                @Pc(4911) String text = stringStack[--stringStackPointer];

                @Pc(10848) StringBuffer buffer = new StringBuffer(text.length());
                @Pc(10850) boolean escaped = false;

                for (@Pc(506) int i = 0; i < text.length(); i++) {
                    @Pc(10857) char c = text.charAt(i);

                    if (c == '<') {
                        escaped = true;
                    } else if (c == '>') {
                        escaped = false;
                    } else if (!escaped) {
                        buffer.append(c);
                    }
                }

                stringStack[stringStackPointer++] = buffer.toString();
                return;
            }

            if (op == STRING_INDEXOF_CHAR) {
                @Pc(4911) String string = stringStack[--stringStackPointer];
                intStackPointer -= 2;

                @Pc(21) int c = intStack[intStackPointer];
                @Pc(27) int fromIndex = intStack[intStackPointer + 1];

                intStack[intStackPointer++] = string.indexOf(c, fromIndex);
                return;
            }

            if (op == STRING_INDEXOF_STRING) {
                stringStackPointer -= 2;

                @Pc(4911) String string = stringStack[stringStackPointer];
                @Pc(1394) String str = stringStack[stringStackPointer + 1];
                @Pc(27) int fromIndex = intStack[--intStackPointer];

                intStack[intStackPointer++] = string.indexOf(str, fromIndex);
                return;
            }

            if (op == CHAR_TOLOWERCASE) {
                @Pc(15) int c = intStack[--intStackPointer];
                intStack[intStackPointer++] = Character.toLowerCase((char) c);
                return;
            }

            if (op == CHAR_TOUPPERCASE) {
                @Pc(15) int c = intStack[--intStackPointer];
                intStack[intStackPointer++] = Character.toUpperCase((char) c);
                return;
            }

            if (op == TOSTRING_LOCALISED) {
                @Pc(575) boolean delimit = intStack[--intStackPointer] != 0;
                @Pc(21) int value = intStack[--intStackPointer];
                stringStack[stringStackPointer++] = StringTools.formatNumber(Client.language, delimit, value, 0);
                return;
            }

            if (op == STRINGWIDTH) {
                @Pc(4911) String string = stringStack[--stringStackPointer];
                @Pc(21) int font = intStack[--intStackPointer];
                @Pc(11077) FontMetrics metrics = FontMetrics.loadGroup(js5.FONTMETRICS, font);

                intStack[intStackPointer++] = metrics.stringWidth(Sprites.nameIcons, string);
                return;
            }

            if (op == FORMAT_DATETIME_FROM_MINUTES) {
                stringStack[stringStackPointer++] = TimeUtils.datetimeFromTime((long) intStack[--intStackPointer] * TimeUtils.MILLISECONDS_PER_MINUTE, Client.language) + " UTC";
                return;
            }

            if (op == CLANFORUMQFC_TOSTRING) {
                @Pc(9705) long local9705 = longStack[--longStackPointer];
                stringStack[stringStackPointer++] = local9705 == -1L ? "" : Long.toString(local9705, 36).toUpperCase();
                return;
            }
        } else if (op < 4300) {
            if (op == OC_NAME) {
                @Pc(15) int id = intStack[--intStackPointer];
                stringStack[stringStackPointer++] = ObjTypeList.instance.list(id).name;
                return;
            }

            if (op == OC_OP) {
                intStackPointer -= 2;

                @Pc(15) int id = intStack[intStackPointer];
                @Pc(21) int opNum = intStack[intStackPointer + 1];
                @Pc(11206) ObjType objType = ObjTypeList.instance.list(id);

                if (opNum >= 1 && opNum <= 5 && objType.op[opNum - 1] != null) {
                    stringStack[stringStackPointer++] = objType.op[opNum - 1];
                    return;
                }

                stringStack[stringStackPointer++] = "";
                return;
            }

            if (op == OC_IOP) {
                intStackPointer -= 2;

                @Pc(15) int id = intStack[intStackPointer];
                @Pc(21) int opNum = intStack[intStackPointer + 1];
                @Pc(11206) ObjType local11206 = ObjTypeList.instance.list(id);

                if (opNum >= 1 && opNum <= 5 && local11206.iop[opNum - 1] != null) {
                    stringStack[stringStackPointer++] = local11206.iop[opNum - 1];
                    return;
                }

                stringStack[stringStackPointer++] = "";
                return;
            }

            if (op == OC_COST) {
                @Pc(15) int id = intStack[--intStackPointer];
                intStack[intStackPointer++] = ObjTypeList.instance.list(id).cost;
                return;
            }

            if (op == OC_STACKABLE) {
                @Pc(15) int id = intStack[--intStackPointer];
                intStack[intStackPointer++] = ObjTypeList.instance.list(id).stackable == 1 ? 1 : 0;
                return;
            }

            if (op == OC_CERT) {
                @Pc(15) int id = intStack[--intStackPointer];
                @Pc(11380) ObjType objType = ObjTypeList.instance.list(id);

                if (objType.certtemplate == -1 && objType.certlink >= 0) {
                    intStack[intStackPointer++] = objType.certlink;
                    return;
                }

                intStack[intStackPointer++] = id;
                return;
            }

            if (op == OC_UNCERT) {
                @Pc(15) int id = intStack[--intStackPointer];
                @Pc(11380) ObjType objType = ObjTypeList.instance.list(id);

                if (objType.certtemplate >= 0 && objType.certlink >= 0) {
                    intStack[intStackPointer++] = objType.certlink;
                    return;
                }

                intStack[intStackPointer++] = id;
                return;
            }

            if (op == OC_MEMBERS) {
                @Pc(15) int id = intStack[--intStackPointer];
                intStack[intStackPointer++] = ObjTypeList.instance.list(id).members ? 1 : 0;
                return;
            }

            if (op == OC_PARAM) {
                intStackPointer -= 2;

                @Pc(15) int id = intStack[intStackPointer];
                @Pc(21) int param = intStack[intStackPointer + 1];
                @Pc(3848) ParamType paramType = ParamTypeList.instance.list(param);

                if (paramType.isString()) {
                    stringStack[stringStackPointer++] = ObjTypeList.instance.list(id).param(paramType.defaultstr, param);
                    return;
                }

                intStack[intStackPointer++] = ObjTypeList.instance.list(id).param(param, paramType.defaultint);
                return;
            }

            if (op == OC_ICURSOR) {
                intStackPointer -= 2;

                @Pc(15) int id = intStack[intStackPointer];
                @Pc(21) int opNum = intStack[intStackPointer + 1] - 1;
                @Pc(11206) ObjType objType = ObjTypeList.instance.list(id);

                if (objType.cursor1iop == opNum) {
                    intStack[intStackPointer++] = objType.icursor1;
                    return;
                }
                if (objType.cursor2iop == opNum) {
                    intStack[intStackPointer++] = objType.icursor2;
                    return;
                }

                intStack[intStackPointer++] = -1;
                return;
            }

            if (op == OC_FIND) {
                @Pc(4911) String text = stringStack[--stringStackPointer];
                @Pc(21) int restrict = intStack[--intStackPointer];
                ObjFinder.find(restrict == 1, text);
                intStack[intStackPointer++] = ObjFinder.resultCount;
                return;
            }

            if (op == OC_FINDNEXT) {
                if (ObjFinder.results != null && ObjFinder.pointer < ObjFinder.resultCount) {
                    intStack[intStackPointer++] = ObjFinder.results[ObjFinder.pointer++] & 0xFFFF;
                    return;
                }

                intStack[intStackPointer++] = -1;
                return;
            }

            if (op == OC_FINDRESTART) {
                ObjFinder.pointer = 0;
                return;
            }

            if (op == OC_MULTISTACKSIZE) {
                @Pc(15) int id = intStack[--intStackPointer];
                intStack[intStackPointer++] = ObjTypeList.instance.list(id).multistacksize;
                return;
            }

            if (op == 4214) {
                @Pc(4911) String text = stringStack[--stringStackPointer];
                intStackPointer -= 3;

                @Pc(21) int restrict = intStack[intStackPointer];
                @Pc(27) int param = intStack[intStackPointer + 1];
                @Pc(506) int expected = intStack[intStackPointer + 2];

                ObjFinder.findIntParam(restrict == 1, expected, param, text);
                intStack[intStackPointer++] = ObjFinder.resultCount;
                return;
            }

            if (op == 4215) {
                stringStackPointer -= 2;
                intStackPointer -= 2;

                @Pc(4911) String text = stringStack[stringStackPointer];
                @Pc(21) int restrict = intStack[intStackPointer];
                @Pc(27) int param = intStack[intStackPointer + 1];
                @Pc(7345) String expected = stringStack[stringStackPointer + 1];

                ObjFinder.findStringParam(text, param, expected, 8, restrict == 1);
                intStack[intStackPointer++] = ObjFinder.resultCount;
                return;
            }
        } else if (op < 4400) {
            if (op == NC_PARAM) {
                intStackPointer -= 2;

                @Pc(15) int id = intStack[intStackPointer];
                @Pc(21) int param = intStack[intStackPointer + 1];
                @Pc(3848) ParamType paramType = ParamTypeList.instance.list(param);

                if (paramType.isString()) {
                    stringStack[stringStackPointer++] = NPCTypeList.instance.list(id).param(paramType.defaultstr, param);
                    return;
                }

                intStack[intStackPointer++] = NPCTypeList.instance.list(id).param(param, paramType.defaultint);
                return;
            }
        } else if (op < 4500) {
            if (op == LC_PARAM) {
                intStackPointer -= 2;

                @Pc(15) int id = intStack[intStackPointer];
                @Pc(21) int param = intStack[intStackPointer + 1];
                @Pc(3848) ParamType paramType = ParamTypeList.instance.list(param);

                if (paramType.isString()) {
                    stringStack[stringStackPointer++] = LocTypeList.instance.list(id).param(paramType.defaultstr, param);
                    return;
                }

                intStack[intStackPointer++] = LocTypeList.instance.list(id).param(paramType.defaultint, param);
                return;
            }
        } else if (op < 4600) {
            if (op == STRUCT_PARAM) {
                intStackPointer -= 2;

                @Pc(15) int id = intStack[intStackPointer];
                @Pc(21) int param = intStack[intStackPointer + 1];
                @Pc(3848) ParamType paramType = ParamTypeList.instance.list(param);

                if (paramType.isString()) {
                    stringStack[stringStackPointer++] = StructTypeList.instance.list(id).param(param, paramType.defaultstr);
                    return;
                }

                intStack[intStackPointer++] = StructTypeList.instance.list(id).param(paramType.defaultint, param);
                return;
            }
        } else if (op < 4700) {
            if (op == BAS_GETANIM_READY) {
                @Pc(15) int id = intStack[--intStackPointer];
                @Pc(12037) BASType basType = BASTypeList.instance.list(id);

                if (basType.readyAnimations != null && basType.readyAnimations.length > 0) {
                    @Pc(27) int bestIndex = 0;
                    @Pc(506) int bestWeight = basType.readyAnimationWeights[0];

                    for (@Pc(2978) int index = 1; index < basType.readyAnimations.length; index++) {
                        if (basType.readyAnimationWeights[index] > bestWeight) {
                            bestIndex = index;
                            bestWeight = basType.readyAnimationWeights[index];
                        }
                    }

                    intStack[intStackPointer++] = basType.readyAnimations[bestIndex];
                    return;
                }

                intStack[intStackPointer++] = basType.ready;
                return;
            }
        } else if (op < 4800) {
            if (op == 4700) {
                intStack[intStackPointer++] = Static587.aBoolean663 ? 1 : 0;
                return;
            }

            if (op == 4701) {
                @Pc(4911) String local4911 = stringStack[--stringStackPointer];

                if (MainLogicManager.step == MainLogicStep.STEP_LOBBY_SCREEN && !LoginManager.inProgress()) {
                    if (local4911.length() > 20) {
                        Static486.aByte115 = -4;
                        return;
                    }

                    Static486.aByte115 = -1;
                    @Pc(5005) ClientMessage local5005 = ClientMessage.create(ClientProt.CLIENT_PROT_90, ServerConnection.LOBBY.isaac);
                    local5005.bitPacket.p1(0);
                    @Pc(27) int local27 = local5005.bitPacket.pos;
                    local5005.bitPacket.pjstr(local4911);
                    local5005.bitPacket.psize1(local5005.bitPacket.pos - local27);
                    ServerConnection.LOBBY.send(local5005);
                    return;
                }

                Static486.aByte115 = -5;
                return;
            }

            if (op == 4702) {
                intStack[intStackPointer++] = Static486.aByte115;
                if (Static486.aByte115 != -1) {
                    Static486.aByte115 = -6;
                }
                return;
            }
        }

        throw new IllegalStateException(String.valueOf(op));
    }

    @OriginalMember(owner = "client!ou", name = "a", descriptor = "(Lclient!hda;)V")
    public static void sendToBack(@OriginalArg(0) Component component) {
        if (component == null) {
            return;
        }

        if (component.id != -1) {
            @Pc(12) Component layer = InterfaceList.list(component.layer);
            if (layer == null) {
                return;
            }

            if (layer.dynamicComponents == layer.staticComponents) {
                layer.dynamicComponents = new Component[layer.staticComponents.length];
                layer.dynamicComponents[0] = component;
                Arrays.copy(layer.staticComponents, 0, layer.dynamicComponents, 1, component.id);
                Arrays.copy(layer.staticComponents, component.id + 1, layer.dynamicComponents, component.id + 1, layer.staticComponents.length - component.id - 1);
                return;
            }

            @Pc(66) int i = 0;
            @Pc(69) Component[] dynamicComponents = layer.dynamicComponents;
            while (i < dynamicComponents.length && dynamicComponents[i] != component) {
                i++;
            }
            if (i >= dynamicComponents.length) {
                return;
            }

            Arrays.copy(dynamicComponents, 0, dynamicComponents, 1, i);
            dynamicComponents[0] = component;
        } else {
            @Pc(106) int parent = component.slot >>> 16;
            @Pc(110) Component[] children = InterfaceList.cache[parent];

            if (children == null) {
                @Pc(69) Component[] newChildren = InterfaceList.interfaces[parent];
                @Pc(119) int length = newChildren.length;
                children = InterfaceList.cache[parent] = new Component[length];
                Arrays.copy(newChildren, 0, children, 0, newChildren.length);
            }

            @Pc(135) int i;
            for (i = 0; i < children.length; i++) {
                if (children[i] == component) {
                    break;
                }
            }
            if (i >= children.length) {
                return;
            }

            Arrays.copy(children, 0, children, 1, i);
            children[0] = component;
        }
    }

    @OriginalMember(owner = "client!ou", name = "b", descriptor = "(IZ)V")
    public static void profileOutput() {
    }

    @OriginalMember(owner = "client!ou", name = "a", descriptor = "(Lclient!fj;I)V")
    public static void executeScript(@OriginalArg(0) ClientScript script, @OriginalArg(1) int maxOps) {
        intStackPointer = 0;
        stringStackPointer = 0;

        @Pc(5) int pc = -1;
        @Pc(8) int[] opcodes = script.opcodes;
        @Pc(11) int[] operands = script.intOperands;
        @Pc(34) int op = -1;

        framePointer = 0;

        try {
            @Pc(17) int opCount = 0;

            while (true) {
                opCount++;
                if (opCount > maxOps) {
                    throw new RuntimeException("slow");
                }

                pc++;
                op = opcodes[pc];

                if (debug && (debugName == null || script.name != null && script.name.indexOf(debugName) != -1)) {
                    System.out.println(script.name + ": " + op);
                }

                if (op >= 150) {
                    @Pc(1436) boolean secondary;
                    if (operands[pc] == 1) {
                        secondary = true;
                    } else {
                        secondary = false;
                    }

                    if (op >= 150 && op < 5000) {
                        handleSmallOp(op, secondary);
                    } else if (op >= 5000 && op < 10000) {
                        handleLargeOp(op, secondary);
                    } else {
                        break;
                    }
                } else if (op == PUSH_CONSTANT_INT) {
                    intStack[intStackPointer++] = operands[pc];
                } else if (op == PUSH_VAR) {
                    @Pc(96) int id = operands[pc];
                    intStack[intStackPointer++] = TimedVarDomain.instance.varValues[id];
                } else if (op == POP_VAR) {
                    @Pc(96) int id = operands[pc];
                    TimedVarDomain.instance.setVarValueInt(id, intStack[--intStackPointer]);
                } else if (op == PUSH_CONSTANT_STRING) {
                    stringStack[stringStackPointer++] = script.stringOperands[pc];
                } else if (op == BRANCH) {
                    pc += operands[pc];
                } else if (op == BRANCH_NOT) {
                    intStackPointer -= 2;
                    if (intStack[intStackPointer] != intStack[intStackPointer + 1]) {
                        pc += operands[pc];
                    }
                } else if (op == BRANCH_EQUALS) {
                    intStackPointer -= 2;
                    if (intStack[intStackPointer] == intStack[intStackPointer + 1]) {
                        pc += operands[pc];
                    }
                } else if (op == BRANCH_LESS_THAN) {
                    intStackPointer -= 2;
                    if (intStack[intStackPointer] < intStack[intStackPointer + 1]) {
                        pc += operands[pc];
                    }
                } else if (op == BRANCH_GREATER_THAN) {
                    intStackPointer -= 2;
                    if (intStack[intStackPointer] > intStack[intStackPointer + 1]) {
                        pc += operands[pc];
                    }
                } else if (op == RETURN) {
                    if (framePointer == 0) {
                        return;
                    }
                    @Pc(270) StackFrame frame = frames[--framePointer];
                    script = frame.script;
                    opcodes = script.opcodes;
                    operands = script.intOperands;
                    pc = frame.pc;
                    intVars = frame.intVars;
                    stringVars = frame.stringVars;
                    longVars = frame.longVars;
                } else if (op == POP_VARBIT) {
                    @Pc(96) int id = operands[pc];
                    intStack[intStackPointer++] = TimedVarDomain.instance.getVarBitValue(id);
                } else if (op == PUSH_VARBIT) {
                    @Pc(96) int id = operands[pc];
                    TimedVarDomain.instance.setVarBitValue(intStack[--intStackPointer], id);
                } else if (op == BRANCH_LESS_THAN_OR_EQUALS) {
                    intStackPointer -= 2;
                    if (intStack[intStackPointer] <= intStack[intStackPointer + 1]) {
                        pc += operands[pc];
                    }
                } else if (op == BRANCH_GREATER_THAN_OR_EQUALS) {
                    intStackPointer -= 2;
                    if (intStack[intStackPointer] >= intStack[intStackPointer + 1]) {
                        pc += operands[pc];
                    }
                } else if (op == PUSH_INT_LOCAL) {
                    intStack[intStackPointer++] = intVars[operands[pc]];
                } else if (op == POP_INT_LOCAL) {
                    intVars[operands[pc]] = intStack[--intStackPointer];
                } else if (op == PUSH_STRING_LOCAL) {
                    stringStack[stringStackPointer++] = stringVars[operands[pc]];
                } else if (op == POP_STRING_LOCAL) {
                    stringVars[operands[pc]] = stringStack[--stringStackPointer];
                } else if (op == JOIN_STRING) {
                    @Pc(96) int count = operands[pc];
                    stringStackPointer -= count;
                    @Pc(465) String string = StringTools.concat(count, stringStackPointer, stringStack);
                    stringStack[stringStackPointer++] = string;
                } else if (op == POP_INT_DISCARD) {
                    intStackPointer--;
                } else if (op == POP_STRING_DISCARD) {
                    stringStackPointer--;
                } else if (op == GOSUB_WITH_PARAMS) {
                    @Pc(96) int id = operands[pc];
                    @Pc(503) ClientScript callee = ClientScriptList.list(id);
                    if (callee == null) {
                        throw new RuntimeException();
                    }

                    @Pc(514) int[] intArgs = new int[callee.intVarCount];
                    @Pc(518) String[] stringArgs = new String[callee.stringVarCount];
                    @Pc(522) long[] longArgs = new long[callee.longVarCount];
                    for (@Pc(524) int i = 0; i < callee.intArgCount; i++) {
                        intArgs[i] = intStack[intStackPointer + i - callee.intArgCount];
                    }
                    for (@Pc(543) int i = 0; i < callee.stringArgCount; i++) {
                        stringArgs[i] = stringStack[stringStackPointer + i - callee.stringArgCount];
                    }
                    for (@Pc(562) int i = 0; i < callee.longArgCount; i++) {
                        longArgs[i] = longStack[longStackPointer + i - callee.longArgCount];
                    }

                    intStackPointer -= callee.intArgCount;
                    stringStackPointer -= callee.stringArgCount;
                    longStackPointer -= callee.longArgCount;

                    @Pc(598) StackFrame frame = new StackFrame();
                    frame.script = script;
                    frame.pc = pc;
                    frame.intVars = intVars;
                    frame.stringVars = stringVars;
                    frame.longVars = longVars;

                    if (framePointer >= frames.length) {
                        throw new RuntimeException();
                    }

                    frames[framePointer++] = frame;
                    script = callee;
                    opcodes = callee.opcodes;
                    operands = callee.intOperands;
                    pc = -1;
                    intVars = intArgs;
                    stringVars = stringArgs;
                    longVars = longArgs;
                } else if (op == PUSH_VARC) {
                    intStack[intStackPointer++] = Static511.varcs[operands[pc]];
                } else if (op == POP_VARC) {
                    @Pc(96) int id = operands[pc];
                    Static511.varcs[id] = intStack[--intStackPointer];
                    DelayedStateChange.resetVarc(id);
                    Static624.varcSaveRecommended |= Static118.permVarcs[id];
                } else if (op == DEFINE_ARRAY) {
                    @Pc(96) int id = operands[pc] >> 16;
                    @Pc(706) int type = operands[pc] & 0xFFFF;
                    @Pc(714) int length = intStack[--intStackPointer];
                    if (length < 0 || length > 5000) {
                        throw new RuntimeException();
                    }

                    arrayLengths[id] = length;

                    @Pc(732) byte v = -1;
                    if (type == 'i') {
                        v = 0;
                    }

                    for (@Pc(739) int i = 0; i < length; i++) {
                        arrayVars[id][i] = v;
                    }
                } else if (op == PUSH_ARRAY_INT) {
                    @Pc(96) int id = operands[pc];
                    @Pc(706) int index = intStack[--intStackPointer];
                    if (index < 0 || index >= arrayLengths[id]) {
                        throw new RuntimeException();
                    }
                    intStack[intStackPointer++] = arrayVars[id][index];
                } else if (op == POP_ARRAY_INT) {
                    @Pc(96) int id = operands[pc];
                    intStackPointer -= 2;
                    @Pc(706) int index = intStack[intStackPointer];
                    if (index < 0 || index >= arrayLengths[id]) {
                        throw new RuntimeException();
                    }
                    arrayVars[id][index] = intStack[intStackPointer + 1];
                } else if (op == PUSH_VARCSTR) {
                    @Pc(843) String varcstr = Static37.varcstrs[operands[pc]];
                    if (varcstr == null) {
                        varcstr = "null";
                    }
                    stringStack[stringStackPointer++] = varcstr;
                } else if (op == POP_VARCSTR) {
                    @Pc(96) int id = operands[pc];
                    Static37.varcstrs[id] = stringStack[--stringStackPointer];
                    DelayedStateChange.resetVarcstr(id);
                } else if (op == SWITCH) {
                    @Pc(889) IterableHashTable table = script.switchTables[operands[pc]];
                    @Pc(902) IntNode jump = (IntNode) table.get(intStack[--intStackPointer]);
                    if (jump != null) {
                        pc += jump.value;
                    }
                } else if (op == PUSH_LONG_CONSTANT) {
                    longStack[longStackPointer++] = script.longOperands[pc];
                } else if (op == POP_LONG_DISCARD) {
                    longStackPointer--;
                } else if (op == PUSH_LONG_LOCAL) {
                    longStack[longStackPointer++] = longVars[operands[pc]];
                } else if (op == POP_LONG_LOCAL) {
                    longVars[operands[pc]] = longStack[--longStackPointer];
                } else if (op == LONG_BRANCH_NOT) {
                    longStackPointer -= 2;
                    if (longStack[longStackPointer] != longStack[longStackPointer + 1]) {
                        pc += operands[pc];
                    }
                } else if (op == LONG_BRANCH_EQUALS) {
                    longStackPointer -= 2;
                    if (longStack[longStackPointer] == longStack[longStackPointer + 1]) {
                        pc += operands[pc];
                    }
                } else if (op == LONG_BRANCH_LESS_THAN) {
                    longStackPointer -= 2;
                    if (longStack[longStackPointer] < longStack[longStackPointer + 1]) {
                        pc += operands[pc];
                    }
                } else if (op == LONG_BRANCH_GREATER_THAN) {
                    longStackPointer -= 2;
                    if (longStack[longStackPointer] > longStack[longStackPointer + 1]) {
                        pc += operands[pc];
                    }
                } else if (op == LONG_BRANCH_LESS_THAN_OR_EQUALS) {
                    longStackPointer -= 2;
                    if (longStack[longStackPointer] <= longStack[longStackPointer + 1]) {
                        pc += operands[pc];
                    }
                } else if (op == LONG_BRANCH_GREATER_THAN_OR_EQUALS) {
                    longStackPointer -= 2;
                    if (longStack[longStackPointer] >= longStack[longStackPointer + 1]) {
                        pc += operands[pc];
                    }
                } else if (op == BRANCH_IF_TRUE) {
                    if (intStack[--intStackPointer] == 1) {
                        pc += operands[pc];
                    }
                } else if (op == BRANCH_IF_FALSE) {
                    if (intStack[--intStackPointer] == 0) {
                        pc += operands[pc];
                    }
                } else if (op == PUSH_VARCLAN) {
                    @Pc(96) int id = operands[pc];
                    @Pc(1178) Integer varclan = (Integer) Static279.clanVars[id];
                    if (varclan == null) {
                        @Pc(1185) VarClanSettingType type = VarClanSettingTypeList.instance.list(id);
                        if (type.dataType == 'i' || type.dataType == '1') {
                            intStack[intStackPointer++] = 0;
                        } else {
                            intStack[intStackPointer++] = -1;
                        }
                    } else {
                        intStack[intStackPointer++] = varclan;
                    }
                } else if (op == PUSH_VARCLANBIT) {
                    @Pc(96) int id = operands[pc];
                    @Pc(1236) VarClanSettingType type = VarClanSettingTypeList.instance.list(id);
                    if (type.dataType != '\u0001') {
                        intStack[intStackPointer++] = 0;
                    }
                    @Pc(1256) Integer varclan = (Integer) Static279.clanVars[type.id];
                    if (varclan == null) {
                        intStack[intStackPointer++] = 0;
                    } else {
                        @Pc(1284) int local1284 = type.end == 31 ? -1 : (0x1 << type.end + 1) - 1;
                        intStack[intStackPointer++] = (varclan & local1284) >>> type.start;
                    }
                } else if (op == PUSH_VARCLAN_LONG) {
                    @Pc(96) int id = operands[pc];
                    @Pc(1311) Long varclan = (Long) Static279.clanVars[id];
                    if (varclan == null) {
                        longStack[longStackPointer++] = -1L;
                    } else {
                        longStack[longStackPointer++] = varclan;
                    }
                } else if (op == PUSH_VARCLAN_STRING) {
                    @Pc(96) int id = operands[pc];
                    @Pc(465) String varclan = (String) Static279.clanVars[id];
                    if (varclan == null) {
                        stringStack[stringStackPointer++] = "";
                    } else {
                        stringStack[stringStackPointer++] = varclan;
                    }
                } else if (op == PUSH_VARCLANSETTING) {
                    intStack[intStackPointer++] = getClanSettingInt(operands[pc]);
                } else if (op == PUSH_VARCLANSETTINGBIT) {
                    intStack[intStackPointer++] = getClanSettingVarbit(operands[pc]);
                } else if (op == PUSH_VARCLAN_SETTING_LONG) {
                    longStack[longStackPointer++] = getClanSettingLong(operands[pc]);
                } else if (op == PUSH_VARCLANSETTING_STRING) {
                    stringStack[stringStackPointer++] = getClanSettingString(operands[pc]);
                }
            }

            throw new IllegalStateException("Command: " + op);
        } catch (@Pc(1479) Exception exception) {
            @Pc(1484) StringBuffer buffer = new StringBuffer(30);
            buffer.append("CS2: ").append(script.key).append(" ");
            for (@Pc(706) int i = framePointer - 1; i >= 0; i--) {
                buffer.append("v: ").append(frames[i].script.key).append(" ");
            }
            buffer.append("op: ").append(op);
            JagException.sendTrace(exception, buffer.toString());
        }
    }

    @OriginalMember(owner = "client!ou", name = "a", descriptor = "(Lclient!pd;)V")
    public static void executeHookInner(@OriginalArg(0) HookRequest arg0) {
        executeHookInner(arg0, 200000);
    }

    @OriginalMember(owner = "client!ou", name = "c", descriptor = "(IZ)V")
    public static void handleLargeOp(@OriginalArg(0) int opcode, @OriginalArg(1) boolean unfocused) {
        if (opcode < 5100) {
            if (opcode == CHAT_GETFILTER_PUBLIC) {
                intStack[intStackPointer++] = Static133.publicChatFilter;
                return;
            }

            if (opcode == CHAT_SETFILTER) {
                intStackPointer -= 3;
                Static133.publicChatFilter = intStack[intStackPointer];
                Static726.privateChatMode = PrivateChatMode.fromId(intStack[intStackPointer + 1]);
                if (Static726.privateChatMode == null) {
                    Static726.privateChatMode = PrivateChatMode.FRIENDS;
                }
                Static87.tradeChatFilter = intStack[intStackPointer + 2];
                @Pc(57) ServerConnection connection = ConnectionManager.active();
                @Pc(63) ClientMessage message = ClientMessage.create(ClientProt.SET_CHATFILTERSETTINGS, connection.isaac);
                message.bitPacket.p1(Static133.publicChatFilter);
                message.bitPacket.p1(Static726.privateChatMode.id);
                message.bitPacket.p1(Static87.tradeChatFilter);
                connection.send(message);
                return;
            }

            if (opcode == CHAT_SENDABUSEREPORT) {
                stringStackPointer -= 2;
                @Pc(95) String name = stringStack[stringStackPointer];
                @Pc(101) String local101 = stringStack[stringStackPointer + 1];
                intStackPointer -= 2;
                @Pc(109) int rule = intStack[intStackPointer];
                @Pc(115) int muted = intStack[intStackPointer + 1];
                if (local101 == null) {
                    local101 = "";
                }
                if (local101.length() > 80) {
                    local101 = local101.substring(0, 80);
                }
                @Pc(135) ServerConnection connection = ConnectionManager.active();
                @Pc(141) ClientMessage message = ClientMessage.create(ClientProt.SEND_SNAPSHOT, connection.isaac);
                message.bitPacket.p1(Packet.pjstrlen(name) + Packet.pjstrlen(local101) + 2);
                message.bitPacket.pjstr(name);
                message.bitPacket.p1(rule - 1);
                message.bitPacket.p1(muted);
                message.bitPacket.pjstr(local101);
                connection.send(message);
                return;
            }

            if (opcode == CHAT_GETHISTORY_BYTYPEANDLINE) {
                @Pc(192) int index = intStack[--intStackPointer];
                @Pc(196) ChatLine line = ChatHistory.getLine(index);
                @Pc(198) String message = "";
                if (line != null && line.message != null) {
                    message = line.message;
                }
                stringStack[stringStackPointer++] = message;
                return;
            }

            if (opcode == CHAT_GETHISTORY_BYUID) {
                @Pc(192) int index = intStack[--intStackPointer];
                @Pc(196) ChatLine line = ChatHistory.getLine(index);
                @Pc(109) int type = -1;
                if (line != null) {
                    type = line.type;
                }
                intStack[intStackPointer++] = type;
                return;
            }

            if (opcode == CHAT_GETFILTER_PRIVATE) {
                if (Static726.privateChatMode == null) {
                    intStack[intStackPointer++] = -1;
                    return;
                }
                intStack[intStackPointer++] = Static726.privateChatMode.id;
                return;
            }

            if (opcode == CHAT_SETMODE) {
                @Pc(192) int local192 = intStack[--intStackPointer];
                @Pc(289) ServerConnection local289 = ConnectionManager.active();
                @Pc(295) ClientMessage local295 = ClientMessage.create(ClientProt.CHAT_SETMODE, local289.isaac);
                local295.bitPacket.p1(local192);
                local289.send(local295);
                return;
            }

            if (opcode == CHAT_SENDPUBLIC) {
                @Pc(95) String message = stringStack[--stringStackPointer];
                sendPublicChat(message, opcode);
                return;
            }

            if (opcode == CHAT_SENDPRIVATE) {
                stringStackPointer -= 2;
                @Pc(95) String recipient = stringStack[stringStackPointer];
                @Pc(101) String text = stringStack[stringStackPointer + 1];
                if (Client.staffModLevel != 0 || (!UserDetail.underage || UserDetail.parentalChatConsent) && !Static617.quickChatWorld) {
                    @Pc(360) ServerConnection connection = ConnectionManager.active();
                    @Pc(366) ClientMessage message = ClientMessage.create(ClientProt.MESSAGE_PRIVATE, connection.isaac);
                    message.bitPacket.p2(0);
                    @Pc(375) int pos = message.bitPacket.pos;
                    message.bitPacket.pjstr(recipient);
                    WordPack.encode(message.bitPacket, text);
                    message.bitPacket.psize2(message.bitPacket.pos - pos);
                    connection.send(message);
                    return;
                }
                return;
            }

            if (opcode == CHAT_GETHISTORYNAME) {
                @Pc(192) int index = intStack[--intStackPointer];
                @Pc(196) ChatLine line = ChatHistory.getLine(index);
                @Pc(198) String name = "";
                if (line != null && line.name != null) {
                    name = line.name;
                }
                stringStack[stringStackPointer++] = name;
                return;
            }

            if (opcode == CHAT_GETHISTORYCLAN) {
                @Pc(192) int index = intStack[--intStackPointer];
                @Pc(196) ChatLine line = ChatHistory.getLine(index);
                @Pc(198) String clan = "";
                if (line != null && line.clan != null) {
                    clan = line.clan;
                }
                stringStack[stringStackPointer++] = clan;
                return;
            }

            if (opcode == CHAT_GETHISTORYPHRASE) {
                @Pc(192) int index = intStack[--intStackPointer];
                @Pc(196) ChatLine line = ChatHistory.getLine(index);
                @Pc(109) int quickChatId = -1;
                if (line != null) {
                    quickChatId = line.quickChatId;
                }
                intStack[intStackPointer++] = quickChatId;
                return;
            }

            if (opcode == CHAT_PLAYERNAME) {
                @Pc(95) String name;
                if (PlayerEntity.self == null || PlayerEntity.self.displayName == null) {
                    name = "";
                } else {
                    name = PlayerEntity.self.getName(false, true);
                }
                stringStack[stringStackPointer++] = name;
                return;
            }

            if (opcode == CHAT_GETFILTER_TRADE) {
                intStack[intStackPointer++] = Static87.tradeChatFilter;
                return;
            }

            if (opcode == CHAT_GETHISTORYLENGTH) {
                intStack[intStackPointer++] = ChatHistory.length();
                return;
            }

            if (opcode == 5018) {
                @Pc(192) int index = intStack[--intStackPointer];
                @Pc(196) ChatLine line = ChatHistory.getLine(index);
                @Pc(109) int flags = 0;
                if (line != null) {
                    flags = line.flags;
                }
                intStack[intStackPointer++] = flags;
                return;
            }

            if (opcode == CHAT_GETHISTORYNAME_UNFILTERED) {
                @Pc(192) int index = intStack[--intStackPointer];
                @Pc(196) ChatLine line = ChatHistory.getLine(index);
                @Pc(198) String name = "";
                if (line != null && line.nameUnfiltered != null) {
                    name = line.nameUnfiltered;
                }
                stringStack[stringStackPointer++] = name;
                return;
            }

            if (opcode == CHAT_PLAYERNAME_UNFILTERED) {
                @Pc(95) String name;
                if (PlayerEntity.self == null || PlayerEntity.self.displayName == null) {
                    name = "";
                } else {
                    name = PlayerEntity.self.getNameUnfiltered();
                }
                stringStack[stringStackPointer++] = name;
                return;
            }

            if (opcode == CHAT_GETNEXTUID) {
                @Pc(192) int index = intStack[--intStackPointer];
                @Pc(196) ChatLine line = ChatHistory.getLine(index);
                @Pc(109) int uid = -1;
                if (line != null) {
                    uid = line.uid;
                }
                intStack[intStackPointer++] = uid;
                return;
            }

            if (opcode == CHAT_GETPREVUID) {
                @Pc(192) int index = intStack[--intStackPointer];
                @Pc(196) ChatLine line = ChatHistory.getLine(index);
                @Pc(109) int clock = -1;
                if (line != null) {
                    clock = line.clock;
                }
                intStack[intStackPointer++] = clock;
                return;
            }

            if (opcode == CHAT_GETHISTORYDISPLAYNAME) {
                @Pc(192) int index = intStack[--intStackPointer];
                @Pc(196) ChatLine line = ChatHistory.getLine(index);
                @Pc(198) String name = "";
                if (line != null && line.displayName != null) {
                    name = line.displayName;
                }
                stringStack[stringStackPointer++] = name;
                return;
            }

            if (opcode == CHATCAT_GETDESC) {
                @Pc(192) int id = intStack[--intStackPointer];
                stringStack[stringStackPointer++] = QuickChatCatTypeList.instance.list(id).desc;
                return;
            }

            if (opcode == CHATCAT_GETSUBCATCOUNT) {
                @Pc(192) int id = intStack[--intStackPointer];
                @Pc(793) QuickChatCatType type = QuickChatCatTypeList.instance.list(id);
                if (type.subcategories == null) {
                    intStack[intStackPointer++] = 0;
                    return;
                }
                intStack[intStackPointer++] = type.subcategories.length;
                return;
            }

            if (opcode == CHATCAT_GETSUBCAT) {
                intStackPointer -= 2;
                @Pc(192) int id = intStack[intStackPointer];
                @Pc(834) int index = intStack[intStackPointer + 1];
                @Pc(839) QuickChatCatType type = QuickChatCatTypeList.instance.list(id);
                @Pc(115) int subcategory = type.subcategories[index];
                intStack[intStackPointer++] = subcategory;
                return;
            }

            if (opcode == CHATCAT_GETPHRASECOUNT) {
                @Pc(192) int id = intStack[--intStackPointer];
                @Pc(793) QuickChatCatType type = QuickChatCatTypeList.instance.list(id);
                if (type.phrases == null) {
                    intStack[intStackPointer++] = 0;
                    return;
                }
                intStack[intStackPointer++] = type.phrases.length;
                return;
            }

            if (opcode == CHATCAT_GETPHRASE) {
                intStackPointer -= 2;
                @Pc(192) int id = intStack[intStackPointer];
                @Pc(834) int index = intStack[intStackPointer + 1];
                intStack[intStackPointer++] = QuickChatCatTypeList.instance.list(id).phrases[index];
                return;
            }

            if (opcode == CHATPHRASE_GETTEXT) {
                @Pc(192) int id = intStack[--intStackPointer];
                stringStack[stringStackPointer++] = QuickChatPhraseTypeList.instance.get(id).getText();
                return;
            }

            if (opcode == CHATPHRASE_GETAUTORESPONSECOUNT) {
                @Pc(192) int id = intStack[--intStackPointer];
                @Pc(966) QuickChatPhraseType type = QuickChatPhraseTypeList.instance.get(id);
                if (type.autoResponses == null) {
                    intStack[intStackPointer++] = 0;
                    return;
                }
                intStack[intStackPointer++] = type.autoResponses.length;
                return;
            }

            if (opcode == CHATPHRASE_GETAUTORESPONSE) {
                intStackPointer -= 2;
                @Pc(192) int id = intStack[intStackPointer];
                @Pc(834) int index = intStack[intStackPointer + 1];
                intStack[intStackPointer++] = QuickChatPhraseTypeList.instance.get(id).autoResponses[index];
                return;
            }

            if (opcode == ACTIVECHATPHRASE_PREPARE) {
                activeChatPhrase = new QuickChatPhrase();
                activeChatPhrase.id = intStack[--intStackPointer];
                activeChatPhrase.type = QuickChatPhraseTypeList.instance.get(activeChatPhrase.id);
                activeChatPhrase.fillerValues = new int[activeChatPhrase.type.getDynamicCommandCount()];
                return;
            }

            if (opcode == ACTIVECHATPHRASE_SENDPUBLIC) { // TODO: same as op5061 but second byte is 0
                @Pc(57) ServerConnection connection = ConnectionManager.active();
                @Pc(63) ClientMessage message = ClientMessage.create(ClientProt.MESSAGE_QUICKCHAT_PUBLIC, connection.isaac);
                message.bitPacket.p1(0);
                @Pc(109) int pos = message.bitPacket.pos;
                message.bitPacket.p1(0);
                message.bitPacket.p2(activeChatPhrase.id);
                activeChatPhrase.type.encode(message.bitPacket, activeChatPhrase.fillerValues);
                message.bitPacket.psize1(message.bitPacket.pos - pos);
                connection.send(message);
                return;
            }

            if (opcode == ACTIVECHATPHRASE_SENDPRIVATE) {
                @Pc(95) String text = stringStack[--stringStackPointer];
                @Pc(289) ServerConnection connection = ConnectionManager.active();
                @Pc(295) ClientMessage message = ClientMessage.create(ClientProt.MESSAGE_QUICKCHAT_PRIVATE, connection.isaac);
                message.bitPacket.p1(0);
                @Pc(115) int pos = message.bitPacket.pos;
                message.bitPacket.pjstr(text);
                message.bitPacket.p2(activeChatPhrase.id);
                activeChatPhrase.type.encode(message.bitPacket, activeChatPhrase.fillerValues);
                message.bitPacket.psize1(message.bitPacket.pos - pos);
                connection.send(message);
                return;
            }

            if (opcode == ACTIVECHATPHRASE_SENDCLAN) {
                @Pc(57) ServerConnection connection = ConnectionManager.active();
                @Pc(63) ClientMessage message = ClientMessage.create(ClientProt.MESSAGE_QUICKCHAT_PUBLIC, connection.isaac);
                message.bitPacket.p1(0);
                @Pc(109) int pos = message.bitPacket.pos;
                message.bitPacket.p1(1);
                message.bitPacket.p2(activeChatPhrase.id);
                activeChatPhrase.type.encode(message.bitPacket, activeChatPhrase.fillerValues);
                message.bitPacket.psize1(message.bitPacket.pos - pos);
                connection.send(message);
                return;
            }

            if (opcode == CHATCAT_GETSUBCATSHORTCUT) {
                intStackPointer -= 2;
                @Pc(192) int id = intStack[intStackPointer];
                @Pc(834) int index = intStack[intStackPointer + 1];
                intStack[intStackPointer++] = QuickChatCatTypeList.instance.list(id).subcategoryShortcuts[index];
                return;
            }

            if (opcode == CHATCAT_GETPHRASESHORTCUT) {
                intStackPointer -= 2;
                @Pc(192) int id = intStack[intStackPointer];
                @Pc(834) int index = intStack[intStackPointer + 1];
                intStack[intStackPointer++] = QuickChatCatTypeList.instance.list(id).phraseShortcuts[index];
                return;
            }

            if (opcode == CHATCAT_FINDSUBCATBYSHORTCUT) {
                intStackPointer -= 2;
                @Pc(192) int id = intStack[intStackPointer];
                @Pc(834) int shortcut = intStack[intStackPointer + 1];
                if (shortcut == -1) {
                    intStack[intStackPointer++] = -1;
                    return;
                }
                intStack[intStackPointer++] = QuickChatCatTypeList.instance.list(id).findSubcategoryByShortcut((char) shortcut);
                return;
            }

            if (opcode == CHATCAT_FINDPHRASEBYSHORTCUT) {
                intStackPointer -= 2;
                @Pc(192) int id = intStack[intStackPointer];
                @Pc(834) int shortcut = intStack[intStackPointer + 1];
                if (shortcut == -1) {
                    intStack[intStackPointer++] = -1;
                    return;
                }
                intStack[intStackPointer++] = QuickChatCatTypeList.instance.list(id).findPhraseByShortcut((char) shortcut);
                return;
            }

            if (opcode == CHATPHRASE_GETDYNAMICCOMMANDCOUNT) {
                @Pc(192) int id = intStack[--intStackPointer];
                intStack[intStackPointer++] = QuickChatPhraseTypeList.instance.get(id).getDynamicCommandCount();
                return;
            }

            if (opcode == CHATPHRASE_GETDYNAMICCOMMAND) {
                intStackPointer -= 2;
                @Pc(192) int id = intStack[intStackPointer];
                @Pc(834) int index = intStack[intStackPointer + 1];
                @Pc(109) int dynamicCommandId = QuickChatPhraseTypeList.instance.get(id).getDynamicCommand(index).id;
                intStack[intStackPointer++] = dynamicCommandId;
                return;
            }

            if (opcode == ACTIVECHATPHRASE_SETDYNAMICINT) {
                intStackPointer -= 2;
                @Pc(192) int id = intStack[intStackPointer];
                @Pc(834) int value = intStack[intStackPointer + 1];
                activeChatPhrase.fillerValues[id] = value;
                return;
            }

            if (opcode == ACTIVECHATPHRASE_SETDYNAMICOBJ) {
                intStackPointer -= 2;
                @Pc(192) int id = intStack[intStackPointer];
                @Pc(834) int value = intStack[intStackPointer + 1];
                activeChatPhrase.fillerValues[id] = value;
                return;
            }

            if (opcode == CHATPHRASE_GETDYNAMICCOMMANDPARAM_ENUM) {
                intStackPointer -= 3;
                @Pc(192) int id = intStack[intStackPointer];
                @Pc(834) int type = intStack[intStackPointer + 1];
                @Pc(109) int index = intStack[intStackPointer + 2];
                @Pc(1526) QuickChatPhraseType phrase = QuickChatPhraseTypeList.instance.get(id);
                if (phrase.getDynamicCommand(type).id != 0) {
                    throw new RuntimeException("bad command");
                }
                intStack[intStackPointer++] = phrase.getDynamicCommandParam(type, index);
                return;
            }

            if (opcode == CHATPHRASE_FIND) {
                @Pc(95) String local95 = stringStack[--stringStackPointer];
                @Pc(1578) boolean local1578 = intStack[--intStackPointer] == 1;
                Static494.method6599(local95, local1578);
                intStack[intStackPointer++] = ObjFinder.resultCount;
                return;
            }

            if (opcode == CHATPHRASE_FINDNEXT) {
                if (ObjFinder.results != null && ObjFinder.pointer < ObjFinder.resultCount) {
                    intStack[intStackPointer++] = ObjFinder.results[ObjFinder.pointer++] & 0xFFFF;
                    return;
                }
                intStack[intStackPointer++] = -1;
                return;
            }

            if (opcode == CHATPHRASE_FINDRESTART) {
                ObjFinder.pointer = 0;
                return;
            }

            if (opcode == 5074) {
                @Pc(57) ServerConnection connection = ConnectionManager.active();
                @Pc(63) ClientMessage message = ClientMessage.create(ClientProt.MESSAGE_QUICKCHAT_PUBLIC, connection.isaac);
                message.bitPacket.p1(0);
                @Pc(109) int local109 = message.bitPacket.pos;
                message.bitPacket.p1(2);
                message.bitPacket.p2(activeChatPhrase.id);
                activeChatPhrase.type.encode(message.bitPacket, activeChatPhrase.fillerValues);
                message.bitPacket.psize1(message.bitPacket.pos - local109);
                connection.send(message);
                return;
            }

            if (opcode == 5075) {
                @Pc(57) ServerConnection connection = ConnectionManager.active();
                @Pc(63) ClientMessage message = ClientMessage.create(ClientProt.MESSAGE_QUICKCHAT_PUBLIC, connection.isaac);
                message.bitPacket.p1(0);
                @Pc(109) int local109 = message.bitPacket.pos;
                message.bitPacket.p1(3);
                message.bitPacket.p2(activeChatPhrase.id);
                activeChatPhrase.type.encode(message.bitPacket, activeChatPhrase.fillerValues);
                message.bitPacket.psize1(message.bitPacket.pos - local109);
                connection.send(message);
                return;
            }
        } else if (opcode < 5200) {
            if (opcode == KEYHELD_ALT) {
                if (KeyboardMonitor.instance.isPressed(SimpleKeyboardMonitor.KEY_CODE_ALT)) {
                    intStack[intStackPointer++] = 1;
                    return;
                }
                intStack[intStackPointer++] = 0;
                return;
            }

            if (opcode == KEYHELD_CTRL) {
                if (KeyboardMonitor.instance.isPressed(SimpleKeyboardMonitor.KEY_CODE_CONTROL)) {
                    intStack[intStackPointer++] = 1;
                    return;
                }
                intStack[intStackPointer++] = 0;
                return;
            }

            if (opcode == KEYHELD_SHIFT) {
                if (KeyboardMonitor.instance.isPressed(SimpleKeyboardMonitor.KEY_CODE_SHIFT)) {
                    intStack[intStackPointer++] = 1;
                    return;
                }
                intStack[intStackPointer++] = 0;
                return;
            }
        } else if (opcode < 5300) {
            if (opcode == WORLDMAP_SETZOOM) {
                WorldMap.setZoomPercentage(intStack[--intStackPointer]);
                return;
            }

            if (opcode == WORLDMAP_GETZOOM) {
                intStack[intStackPointer++] = WorldMap.getZoom();
                return;
            }

            if (opcode == WORLDMAP_SETMAP) {
                WorldMap.setMap(intStack[--intStackPointer], false, -1, -1);
                return;
            }

            if (opcode == WORLDMAP_GETMAP) {
                @Pc(192) int coord = intStack[--intStackPointer];
                @Pc(1908) WorldMapArea area = WorldMap.getMap((coord >> 14) & 0x3FFF, coord & 0x3FFF);
                if (area == null) {
                    intStack[intStackPointer++] = -1;
                    return;
                }
                intStack[intStackPointer++] = area.id;
                return;
            }

            if (opcode == ORLDMAP_GETMAPNAME) {
                @Pc(1942) WorldMapArea area = WorldMap.getArea(intStack[--intStackPointer]);
                if (area != null && area.name != null) {
                    stringStack[stringStackPointer++] = area.name;
                    return;
                }
                stringStack[stringStackPointer++] = "";
                return;
            }

            if (opcode == WORLDMAP_GETSIZE) {
                intStack[intStackPointer++] = WorldMap.width;
                intStack[intStackPointer++] = WorldMap.height;
                return;
            }

            if (opcode == WORLDMAP_GETDISPLAYPOSITION) {
                intStack[intStackPointer++] = WorldMap.displayX + WorldMap.areaX;
                intStack[intStackPointer++] = WorldMap.displayZ + WorldMap.areaZ;
                return;
            }

            if (opcode == WORLDMAP_GETCONFIGORIGIN) {
                @Pc(192) int id = intStack[--intStackPointer];
                @Pc(1908) WorldMapArea area = WorldMap.getArea(id);
                if (area == null) {
                    intStack[intStackPointer++] = 0;
                    intStack[intStackPointer++] = 0;
                    return;
                }
                intStack[intStackPointer++] = (area.origin >> 14) & 0x3FFF;
                intStack[intStackPointer++] = area.origin & 0x3FFF;
                return;
            }

            if (opcode == WORLDMAP_GETCONFIGBOUNDS) {
                @Pc(192) int id = intStack[--intStackPointer];
                @Pc(1908) WorldMapArea area = WorldMap.getArea(id);
                if (area == null) {
                    intStack[intStackPointer++] = 0;
                    intStack[intStackPointer++] = 0;
                    return;
                }
                intStack[intStackPointer++] = area.chunkMaxX - area.chunkMinX;
                intStack[intStackPointer++] = area.chunkMaxZ - area.chunkMinZ;
                return;
            }

            if (opcode == WORLDMAP_LISTELEMENT_START) {
                @Pc(2139) MapElementListEntry entry = WorldMap.startElement();
                if (entry == null) {
                    intStack[intStackPointer++] = -1;
                    intStack[intStackPointer++] = -1;
                    return;
                }
                intStack[intStackPointer++] = entry.id;
                @Pc(834) int coord = (entry.level << 28) | ((entry.x + WorldMap.areaX) << 14) | (entry.z + WorldMap.areaZ);
                intStack[intStackPointer++] = coord;
                return;
            }

            if (opcode == WORLDMAP_LISTELEMENT_NEXT) {
                @Pc(2139) MapElementListEntry entry = WorldMap.nextElement();
                if (entry == null) {
                    intStack[intStackPointer++] = -1;
                    intStack[intStackPointer++] = -1;
                    return;
                }
                intStack[intStackPointer++] = entry.id;
                @Pc(834) int coord = (entry.level << 28) | ((entry.x + WorldMap.areaX) << 14) | (entry.z + WorldMap.areaZ);
                intStack[intStackPointer++] = coord;
                return;
            }

            if (opcode == WORLDMAP_JUMPTOSOURCECOORD) {
                @Pc(192) int id = intStack[--intStackPointer];
                @Pc(1908) WorldMapArea area = WorldMap.getArea();
                if (area != null) {
                    @Pc(2289) boolean projected = area.projectDisplay(areaCoord, (id >> 28) & 0x3, (id >> 14) & 0x3FFF, id & 0x3FFF);
                    if (projected) {
                        WorldMap.jumpToDisplayCoord(areaCoord[1], areaCoord[2]);
                    }
                }
                return;
            }

            if (opcode == WORLDMAP_COORDINMAP) {
                intStackPointer -= 2;
                @Pc(192) int coord = intStack[intStackPointer];
                @Pc(834) int id = intStack[intStackPointer + 1];
                @Pc(2329) Queue queue = WorldMap.findAreas((coord >> 14) & 0x3FFF, coord & 0x3FFF);
                @Pc(2331) boolean contained = false;
                for (@Pc(2336) WorldMapArea area = (WorldMapArea) queue.first(); area != null; area = (WorldMapArea) queue.next()) {
                    if (area.id == id) {
                        contained = true;
                        break;
                    }
                }
                if (contained) {
                    intStack[intStackPointer++] = 1;
                    return;
                }
                intStack[intStackPointer++] = 0;
                return;
            }

            if (opcode == WORLDMAP_GETCONFIGZOOM) {
                @Pc(192) int id = intStack[--intStackPointer];
                @Pc(1908) WorldMapArea area = WorldMap.getArea(id);
                if (area == null) {
                    intStack[intStackPointer++] = -1;
                    return;
                }
                intStack[intStackPointer++] = area.zoom;
                return;
            }

            if (opcode == WORLDMAP_ISLOADED) {
                intStack[intStackPointer++] = WorldMap.loadingPercent == 100 ? 1 : 0;
                return;
            }

            if (opcode == WORLDMAP_JUMPTODISPLAYCOORD) {
                @Pc(192) int coord = intStack[--intStackPointer];
                WorldMap.jumpToDisplayCoord((coord >> 14) & 0x3FFF, coord & 0x3FFF);
                return;
            }

            if (opcode == WORLDMAP_GETSOURCEPOSITION) {
                @Pc(1942) WorldMapArea area = WorldMap.getArea();
                if (area != null) {
                    @Pc(1578) boolean projected = area.projectSource(areaCoord, WorldMap.displayX + WorldMap.areaX, WorldMap.displayZ + WorldMap.areaZ);
                    if (projected) {
                        intStack[intStackPointer++] = areaCoord[1];
                        intStack[intStackPointer++] = areaCoord[2];
                        return;
                    }
                    intStack[intStackPointer++] = -1;
                    intStack[intStackPointer++] = -1;
                    return;
                }
                intStack[intStackPointer++] = -1;
                intStack[intStackPointer++] = -1;
                return;
            }

            if (opcode == WORLDMAP_SETMAP_COORD) {
                intStackPointer -= 2;
                @Pc(192) int id = intStack[intStackPointer];
                @Pc(834) int coord = intStack[intStackPointer + 1];
                WorldMap.setMap(id, false, coord & 0x3FFF, coord >> 14 & 0x3FFF);
                return;
            }

            if (opcode == WORLDMAP_GETDISPLAYCOORD) {
                @Pc(192) int coord = intStack[--intStackPointer];
                @Pc(1908) WorldMapArea area = WorldMap.getArea();
                if (area != null) {
                    @Pc(2289) boolean projected = area.projectDisplay(areaCoord, coord >> 28 & 0x3, coord >> 14 & 0x3FFF, coord & 0x3FFF);
                    if (projected) {
                        intStack[intStackPointer++] = areaCoord[1];
                        intStack[intStackPointer++] = areaCoord[2];
                        return;
                    }
                    intStack[intStackPointer++] = -1;
                    intStack[intStackPointer++] = -1;
                    return;
                }
                intStack[intStackPointer++] = -1;
                intStack[intStackPointer++] = -1;
                return;
            }

            if (opcode == WORLDMAP_GETSOURCECOORD) {
                @Pc(192) int coord = intStack[--intStackPointer];
                @Pc(1908) WorldMapArea area = WorldMap.getArea();
                if (area != null) {
                    @Pc(2289) boolean projected = area.projectSource(areaCoord, (coord >> 14) & 0x3FFF, coord & 0x3FFF);
                    if (projected) {
                        intStack[intStackPointer++] = areaCoord[1];
                        intStack[intStackPointer++] = areaCoord[2];
                        return;
                    }
                    intStack[intStackPointer++] = -1;
                    intStack[intStackPointer++] = -1;
                    return;
                }
                intStack[intStackPointer++] = -1;
                intStack[intStackPointer++] = -1;
                return;
            }

            if (opcode == WORLDMAP_FLASHELEMENT) {
                WorldMap.flashElement(intStack[--intStackPointer]);
                return;
            }

            if (opcode == WORLDMAP_SETMAP_COORD_OVERRIDE) {
                intStackPointer -= 2;
                @Pc(192) int id = intStack[intStackPointer];
                @Pc(834) int coord = intStack[intStackPointer + 1];
                WorldMap.setMap(id, true, coord & 0x3FFF, (coord >> 14) & 0x3FFF);
                return;
            }

            if (opcode == WORLDMAP_DISABLEELEMENTS) {
                WorldMap.disableElements = intStack[--intStackPointer] == 1;
                return;
            }

            if (opcode == WORLDMAP_GETDISABLEELEMENTS) {
                intStack[intStackPointer++] = WorldMap.disableElements ? 1 : 0;
                return;
            }

            if (opcode == WORLDMAP_FLASHELEMENTCATEGORY) {
                @Pc(192) int id = intStack[--intStackPointer];
                WorldMap.flashElementCategory(id);
                return;
            }

            if (opcode == WORLDMAP_DISABLEELEMENTCATEGORY) {
                intStackPointer -= 2;
                @Pc(192) int id = intStack[intStackPointer];
                @Pc(1578) boolean disabled = intStack[intStackPointer + 1] == 1;

                if (WorldMap.disabledElementCategories != null) {
                    @Pc(2867) Node node = WorldMap.disabledElementCategories.get(id);
                    if (node != null && !disabled) {
                        node.unlink();
                        return;
                    }

                    if (node == null && disabled) {
                        node = new Node();
                        WorldMap.disabledElementCategories.put(id, node);
                    }
                }
                return;
            }

            if (opcode == WORLDMAP_GETDISABLEELEMENTCATEGORY) {
                @Pc(192) int id = intStack[--intStackPointer];
                if (WorldMap.disabledElementCategories != null) {
                    @Pc(2914) Node node = WorldMap.disabledElementCategories.get(id);
                    intStack[intStackPointer++] = node != null ? 1 : 0;
                    return;
                }
                intStack[intStackPointer++] = 0;
                return;
            }

            if (opcode == WORLDMAP_DISABLEELEMENT) {
                intStackPointer -= 2;
                @Pc(192) int id = intStack[intStackPointer];
                @Pc(1578) boolean disabled = intStack[intStackPointer + 1] == 1;

                if (WorldMap.disabledElements != null) {
                    @Pc(2867) Node node = WorldMap.disabledElements.get(id);
                    if (node != null && !disabled) {
                        node.unlink();
                        return;
                    }

                    if (node == null && disabled) {
                        node = new Node();
                        WorldMap.disabledElements.put(id, node);
                    }
                }
                return;
            }

            if (opcode == WORLDMAP_GETDISABLEELEMENT) {
                @Pc(192) int id = intStack[--intStackPointer];
                if (WorldMap.disabledElements != null) {
                    @Pc(2914) Node node = WorldMap.disabledElements.get(id);
                    intStack[intStackPointer++] = node != null ? 1 : 0;
                    return;
                }
                intStack[intStackPointer++] = 0;
                return;
            }

            if (opcode == WORLDMAP_GETCURRENTMAP) {
                intStack[intStackPointer++] = WorldMap.area == null ? -1 : WorldMap.area.id;
                return;
            }

            if (opcode == WORLDMAP_FINDNEARESTELEMENT) {
                intStackPointer -= 2;
                @Pc(192) int local192 = intStack[intStackPointer];
                @Pc(834) int coord = intStack[intStackPointer + 1];
                @Pc(109) int x = coord >> 14 & 0x3FFF;
                @Pc(115) int z = coord & 0x3FFF;
                @Pc(375) int local375 = WorldMap.findNearestElement(local192, z, x);
                if (local375 < 0) {
                    intStack[intStackPointer++] = -1;
                    return;
                }
                intStack[intStackPointer++] = local375;
                return;
            }

            if (opcode == WORLDMAP_CLOSEMAP) {
                WorldMap.close();
                return;
            }
        } else if (opcode < 5400) {
            if (opcode == FULLSCREEN_ENTER) {
                intStackPointer -= 2;
                @Pc(192) int width = intStack[intStackPointer];
                @Pc(834) int height = intStack[intStackPointer + 1];
                InterfaceManager.changeWindowMode(WindowMode.FULLSCREEN, width, height, false);
                intStack[intStackPointer++] = GameShell.fsframe == null ? 0 : 1;
                return;
            }

            if (opcode == FULLSCREEN_EXIT) {
                if (GameShell.fsframe != null) {
                    InterfaceManager.changeWindowMode(ClientOptions.instance.screenSizeDefault.getValue(), -1, -1, false);
                }
                return;
            }

            if (opcode == FULLSCREEN_MODECOUNT) {
                @Pc(3186) FullscreenMode[] modes = Fullscreen.getModes();
                intStack[intStackPointer++] = modes.length;
                return;
            }

            if (opcode == FULLSCREEN_GETMODE) {
                @Pc(192) int index = intStack[--intStackPointer];
                @Pc(3210) FullscreenMode[] modes = Fullscreen.getModes();
                intStack[intStackPointer++] = modes[index].width;
                intStack[intStackPointer++] = modes[index].height;
                return;
            }

            if (opcode == FULLSCREEN_LASTMODE) {
                @Pc(192) int lastWidth = GameShell.lastFullscreenWidth;
                @Pc(834) int lastHeight = GameShell.lastFullscreenHeight;
                @Pc(109) int index = -1;
                @Pc(3245) FullscreenMode[] modes = Fullscreen.getModes();

                for (@Pc(375) int i = 0; i < modes.length; i++) {
                    @Pc(3252) FullscreenMode mode = modes[i];

                    if (mode.width == lastWidth && mode.height == lastHeight) {
                        index = i;
                        break;
                    }
                }

                intStack[intStackPointer++] = index;
                return;
            }

            if (opcode == GETWINDOWMODE) {
                intStack[intStackPointer++] = InterfaceManager.getWindowMode();
                return;
            }

            if (opcode == SETWINDOWMODE) {
                @Pc(192) int mode = intStack[--intStackPointer];
                if (mode >= 1 && mode <= 2) {
                    InterfaceManager.changeWindowMode(mode, -1, -1, false);
                    return;
                }
                return;
            }

            if (opcode == GETDEFAULTWINDOWMODE) {
                intStack[intStackPointer++] = ClientOptions.instance.screenSizeDefault.getValue();
                return;
            }

            if (opcode == SETDEFAULTWINDOWMODE) {
                @Pc(192) int local192 = intStack[--intStackPointer];
                if (local192 >= 1 && local192 <= 2) {
                    ClientOptions.instance.update(local192, ClientOptions.instance.screenSizeDefault);
                    ClientOptions.instance.update(local192, ClientOptions.instance.screenSize);
                    ClientOptions.save();
                    return;
                }
                return;
            }
        } else if (opcode < 5500) {
            if (opcode == OPENURL) {
                stringStackPointer -= 2;
                @Pc(95) String local95 = stringStack[stringStackPointer];
                @Pc(101) String local101 = stringStack[stringStackPointer + 1];
                @Pc(109) int local109 = intStack[--intStackPointer];
                @Pc(3411) ServerConnection local3411 = ConnectionManager.active();
                @Pc(3417) ClientMessage local3417 = ClientMessage.create(ClientProt.URL_REQUEST, local3411.isaac);
                local3417.bitPacket.p1(Packet.pjstrlen(local95) + Packet.pjstrlen(local101) + 1);
                local3417.bitPacket.pjstr(local95);
                local3417.bitPacket.pjstr(local101);
                local3417.bitPacket.p1(local109);
                local3411.send(local3417);
                return;
            }

            if (opcode == SETCLIENTPALETTE) {
                intStackPointer -= 2;
                Client.clientpalette[intStack[intStackPointer]] = (short) ColourUtils.rgbToHsl(intStack[intStackPointer + 1]);
                ObjTypeList.instance.modelCacheReset();
                ObjTypeList.instance.spriteCacheReset();
                NPCTypeList.instance.modelCacheReset();
                InterfaceManager.redrawAll();
                return;
            }

            if (opcode == SPLINE_NEW) {
                intStackPointer -= 2;
                @Pc(192) int local192 = intStack[intStackPointer];
                @Pc(834) int local834 = intStack[intStackPointer + 1];
                if (local192 >= 0 && local192 < 2) {
                    Camera.spline[local192] = new int[local834 << 1][4];
                }
                return;
            }

            if (opcode == SPLINE_ADDPOINT) {
                intStackPointer -= 7;
                @Pc(192) int local192 = intStack[intStackPointer];
                @Pc(834) int local834 = intStack[intStackPointer + 1] << 1;
                @Pc(109) int local109 = intStack[intStackPointer + 2];
                @Pc(115) int local115 = intStack[intStackPointer + 3];
                @Pc(375) int local375 = intStack[intStackPointer + 4];
                @Pc(3561) int local3561 = intStack[intStackPointer + 5];
                @Pc(3567) int local3567 = intStack[intStackPointer + 6];
                if (local192 >= 0 && local192 < 2 && Camera.spline[local192] != null && local834 >= 0 && local834 < Camera.spline[local192].length) {
                    Camera.spline[local192][local834] = new int[]{(local109 >> 14 & 0x3FFF) << 9, local115 << 2, (local109 & 0x3FFF) << 9, local3567};
                    Camera.spline[local192][local834 + 1] = new int[]{(local375 >> 14 & 0x3FFF) << 9, local3561 << 2, (local375 & 0x3FFF) << 9};
                }
                return;
            }

            if (opcode == SPLINE_LENGTH) {
                @Pc(192) int local192 = Camera.spline[intStack[--intStackPointer]].length >> 1;
                intStack[intStackPointer++] = local192;
                return;
            }
            if (opcode == QUIT) {
                if (GameShell.fsframe != null) {
                    InterfaceManager.changeWindowMode(ClientOptions.instance.screenSizeDefault.getValue(), -1, -1, false);
                }
                if (GameShell.frame != null) {
                    Static266.saveVarcs();
                    System.exit(0);
                    return;
                }
                @Pc(95) String url = Client.quitUrl == null ? Static659.method8605() : Client.quitUrl;
                Static664.openjs(ClientOptions.instance.toolkit.getValue() == ToolkitType.GL, url, false, GameShell.signLink);
                return;
            }

            if (opcode == LASTLOGIN) {
                @Pc(95) String local95 = "";
                if (Static439.hostnameResource != null) {
                    if (Static439.hostnameResource.result == null) {
                        local95 = WebTools.ipDecode(Static439.hostnameResource.intData1);
                    } else {
                        local95 = (String) Static439.hostnameResource.result;
                    }
                }
                stringStack[stringStackPointer++] = local95;
                return;
            }

            if (opcode == 5420) {
                intStack[intStackPointer++] = GameShell.signLink.signed ? 0 : 1;
                return;
            }

            if (opcode == OPENURL_NOLOGIN) {
                if (GameShell.fsframe != null) {
                    InterfaceManager.changeWindowMode(ClientOptions.instance.screenSizeDefault.getValue(), -1, -1, false);
                }
                @Pc(95) String local95 = stringStack[--stringStackPointer];
                @Pc(1578) boolean loggedIn = intStack[--intStackPointer] == 1;
                @Pc(198) String page = Static659.method8605() + local95;
                Static664.openjs(ClientOptions.instance.toolkit.getValue() == ToolkitType.GL, page, loggedIn, GameShell.signLink);
                return;
            }

            if (opcode == 5422) {
                stringStackPointer -= 2;
                @Pc(95) String local95 = stringStack[stringStackPointer];
                @Pc(101) String local101 = stringStack[stringStackPointer + 1];
                @Pc(109) int local109 = intStack[--intStackPointer];
                if (local95.length() > 0) {
                    if (Static685.prefixTitles == null) {
                        Static685.prefixTitles = new String[Static390.anIntArray476[Client.modeGame.id]];
                    }
                    Static685.prefixTitles[local109] = local95;
                }
                if (local101.length() > 0) {
                    if (Static377.suffixTitles == null) {
                        Static377.suffixTitles = new String[Static390.anIntArray476[Client.modeGame.id]];
                    }
                    Static377.suffixTitles[local109] = local101;
                }
                return;
            }

            if (opcode == WRITECONSOLE) {
                System.out.println(stringStack[--stringStackPointer]);
                return;
            }

            if (opcode == FORMATMINIMENU) {
                intStackPointer -= 11;
                MiniMenu.topColour = intStack[intStackPointer];
                MiniMenu.topOpacity = intStack[intStackPointer + 1];
                MiniMenu.spriteBodyColour = intStack[intStackPointer + 2];
                MiniMenu.spriteBodyOpacity = intStack[intStackPointer + 3];
                MiniMenu.separatorSpriteId = intStack[intStackPointer + 4];
                MiniMenu.topCornerSpriteId = intStack[intStackPointer + 5];
                MiniMenu.horizontalBorderSpriteId = intStack[intStackPointer + 6];
                MiniMenu.verticalBorderSpriteId = intStack[intStackPointer + 7];
                MiniMenu.bottomCornerSpriteId = intStack[intStackPointer + 8];
                MiniMenu.textColour = intStack[intStackPointer + 9];
                MiniMenu.spriteHighlightColour = intStack[intStackPointer + 10];
                js5.SPRITES.fileready(MiniMenu.separatorSpriteId);
                js5.SPRITES.fileready(MiniMenu.topCornerSpriteId);
                js5.SPRITES.fileready(MiniMenu.horizontalBorderSpriteId);
                js5.SPRITES.fileready(MiniMenu.verticalBorderSpriteId);
                js5.SPRITES.fileready(MiniMenu.bottomCornerSpriteId);
                MiniMenu.bottomBorderSprite = null;
                MiniMenu.bottomRightCornerSprite = null;
                MiniMenu.bottomLeftCornerSprite = null;
                MiniMenu.topRightCornerSprite = null;
                MiniMenu.topLeftCornerSprite = null;
                MiniMenu.separatorSprite = null;
                MiniMenu.rightBorderSprite = null;
                MiniMenu.leftBorderSprite = null;
                MiniMenu.useSprites = true;
                return;
            }

            if (opcode == DEFAULTMINIMENU) {
                MiniMenu.resetSprites();
                MiniMenu.useSprites = false;
                return;
            }

            if (opcode == SETDEFAULTCURSORS) {
                intStackPointer -= 2;
                Cursor.dflt = intStack[intStackPointer];
                Cursor.interaction = intStack[intStackPointer + 1];
                return;
            }

            if (opcode == SETHARDCODEDOPCURSORS) {
                intStackPointer -= 2;
                return;
            }

            if (opcode == MINIMENUOPEN) {
                intStackPointer -= 2;
                @Pc(192) int idAndSlot = intStack[intStackPointer];
                @Pc(834) int local834 = intStack[intStackPointer + 1];
                intStack[intStackPointer++] = MiniMenu.isOpen(idAndSlot, local834) ? 1 : 0;
                return;
            }

            if (opcode == DOCHEAT) {
                debugconsole.executeComand(false, false, stringStack[--stringStackPointer]);
                return;
            }

            if (opcode == NOTIFY_ACCOUNTCREATED) {
                try {
                    JavaScript.call("accountcreated", GameShell.loaderApplet);
                    return;
                } catch (@Pc(4148) Throwable ignored) {
                    return;
                }
            }

            if (opcode == NOTIFY_ACCOUNTCREATESTARTED) {
                try {
                    JavaScript.call("accountcreatestarted", GameShell.loaderApplet);
                    return;
                } catch (@Pc(4161) Throwable ignored) {
                    return;
                }
            }

            if (opcode == GETCLIPBOARD) {
                @Pc(95) String contents = "";
                if (client.clipboard != null) {
                    @Pc(4173) Transferable transferable = client.clipboard.getContents(null);
                    if (transferable != null) {
                        try {
                            contents = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                            if (contents == null) {
                                contents = "";
                            }
                        } catch (@Pc(4186) Exception local4186) {
                        }
                    }
                }
                stringStack[stringStackPointer++] = contents;
                return;
            }

            if (opcode == SETSUBMENUMINLENGTH) {
                MiniMenu.subMenuMinLength = intStack[--intStackPointer];
                return;
            }

            if (opcode == 5435) {
                intStack[intStackPointer++] = Client.js ? 1 : 0;
                return;
            }

            if (opcode == CAN_RUN_JAVA_CLIENT) {
                if (SystemInfo.instance.javaRelease < 6) {
                    intStack[intStackPointer++] = 0;
                    return;
                }
                if (SystemInfo.instance.javaRelease == 6 && SystemInfo.instance.javaUpdate < 10) {
                    intStack[intStackPointer++] = 0;
                    return;
                }
                intStack[intStackPointer++] = 1;
                return;
            }
        } else if (opcode < 5600) {
            if (opcode == CAM_MOVETO) {
                intStackPointer -= 4;
                @Pc(192) int coord = intStack[intStackPointer];
                @Pc(834) int y = intStack[intStackPointer + 1];
                @Pc(109) int step = intStack[intStackPointer + 2];
                @Pc(115) int rate = intStack[intStackPointer + 3];
                Camera.moveTo((coord >> 14 & 0x3FFF) - WorldMap.areaBaseX, y << 2, (coord & 0x3FFF) - WorldMap.areaBaseZ, step, rate, false);
                return;
            }

            if (opcode == CAM_LOOKAT) {
                intStackPointer -= 4;
                @Pc(192) int coord = intStack[intStackPointer];
                @Pc(834) int y = intStack[intStackPointer + 1];
                @Pc(109) int step = intStack[intStackPointer + 2];
                @Pc(115) int rate = intStack[intStackPointer + 3];
                Camera.lookAt((coord >> 14 & 0x3FFF) - WorldMap.areaBaseX, y << 2, (coord & 0x3FFF) - WorldMap.areaBaseZ, step, rate);
                return;
            }

            if (opcode == CAM_MOVEALONG) {
                intStackPointer -= 6;
                @Pc(192) int pos = intStack[intStackPointer];
                if (pos >= 2) {
                    throw new RuntimeException();
                }

                Camera.posSpline = pos;
                @Pc(834) int posOffset = intStack[intStackPointer + 1];
                if (posOffset + 1 >= Camera.spline[Camera.posSpline].length >> 1) {
                    throw new RuntimeException();
                }

                Camera.splinePosOffset = posOffset;
                Camera.splineRate = 0;
                Camera.splineStart = intStack[intStackPointer + 2];
                Camera.splineEnd = intStack[intStackPointer + 3];
                @Pc(109) int look = intStack[intStackPointer + 4];
                if (look >= 2) {
                    throw new RuntimeException();
                }

                Camera.lookSpline = look;
                @Pc(115) int lookOffset = intStack[intStackPointer + 5];
                if (lookOffset + 1 >= Camera.spline[Camera.lookSpline].length >> 1) {
                    throw new RuntimeException();
                }

                Camera.splineLookOffset = lookOffset;
                Camera.mode = CameraMode.MODE_SPLINE;
                Camera.anInt10383 = -1;
                Camera.anInt10376 = -1;
                return;
            }

            if (opcode == CAM_RESET) {
                Camera.reset();
                return;
            }

            if (opcode == CAM_FORCEANGLE) {
                intStackPointer -= 2;
                Camera.forceAngle(intStack[intStackPointer], intStack[intStackPointer + 1], 0);
                return;
            }

            if (opcode == CAM_GETANGLE_XA) {
                intStack[intStackPointer++] = (int) Camera.playerCameraPitch >> 3;
                return;
            }

            if (opcode == CAM_GETANGLE_YA) {
                intStack[intStackPointer++] = (int) Camera.playerCameraYaw >> 3;
                return;
            }

            if (opcode == CAM_INC_Y) {
                Camera.increaseY();
                return;
            }

            if (opcode == CAM_DEC_Y) {
                Camera.decreaseY();
                return;
            }

            if (opcode == CAM_INC_X) {
                Camera.increaseX();
                return;
            }

            if (opcode == CAM_DEC_X) {
                Camera.decreaseX();
                return;
            }

            if (opcode == CAM_FOLLOWCOORD) {
                @Pc(192) int local192 = intStack[--intStackPointer];
                @Pc(834) int local834 = local192 >> 14 & 0x3FFF;
                @Pc(109) int local109 = local192 & 0x3FFF;
                local834 -= WorldMap.areaBaseX;
                if (local834 < 0) {
                    local834 = 0;
                } else if (local834 >= Static720.mapWidth) {
                    local834 = Static720.mapWidth;
                }
                local109 -= WorldMap.areaBaseZ;
                if (local109 < 0) {
                    local109 = 0;
                } else if (local109 >= Static501.mapLength) {
                    local109 = Static501.mapLength;
                }
                Camera.anInt6262 = (local834 << 9) + 256;
                Camera.anInt4018 = (local109 << 9) + 256;
                Camera.mode = CameraMode.MODE_FOLLOWCOORD;
                Camera.anInt10383 = -1;
                Camera.anInt10376 = -1;
                return;
            }

            if (opcode == CAM_SMOOTH_RESET) {
                Camera.smoothReset();
                return;
            }

            if (opcode == 5514) {
                OrthoMode.zoom = intStack[--intStackPointer];
                return;
            }

            if (opcode == 5516) {
                intStack[intStackPointer++] = OrthoMode.zoom;
                return;
            }

            if (opcode == CAM_REMOVEROOF) {
                @Pc(192) int local192 = intStack[--intStackPointer];
                if (local192 == -1) {
                    @Pc(834) int local834 = local192 >> 14 & 0x3FFF;
                    @Pc(109) int local109 = local192 & 0x3FFF;
                    local834 -= WorldMap.areaBaseX;
                    if (local834 < 0) {
                        local834 = 0;
                    } else if (local834 >= Static720.mapWidth) {
                        local834 = Static720.mapWidth;
                    }
                    local109 -= WorldMap.areaBaseZ;
                    if (local109 < 0) {
                        local109 = 0;
                    } else if (local109 >= Static501.mapLength) {
                        local109 = Static501.mapLength;
                    }
                    Camera.anInt10376 = (local834 << 9) + 256;
                    Camera.anInt10383 = (local109 << 9) + 256;
                    return;
                }
                Camera.anInt10376 = -1;
                Camera.anInt10383 = -1;
                return;
            }

            if (opcode == 5547) {
                intStack[intStackPointer++] = Camera.mode == CameraMode.MODE_DEFAULT ? 1 : 0;
                return;
            }
        } else if (opcode < 5700) {
            if (opcode == LOGIN_REQUEST) {
                stringStackPointer -= 2;
                @Pc(95) String local95 = stringStack[stringStackPointer];
                @Pc(101) String local101 = stringStack[stringStackPointer + 1];
                @Pc(109) int local109 = intStack[--intStackPointer];
                LoginManager.requestLoginWithUsername(local109, local101, local95);
                return;
            }

            if (opcode == LOGIN_CONTINUE) {
                LoginManager.videoAdvertisementFinished();
                return;
            }

            if (opcode == LOGIN_RESETREPLY) {
                if (!LoginManager.inProgress()) {
                    LoginManager.reset();
                }
                return;
            }

            if (opcode == CREATE_AVAILABLEREQUEST) {
                stringStackPointer--;
                if (MainLogicManager.step != 3) {
                    return;
                }
                if (!LoginManager.inProgress() && LobbyManager.step == 0) {
                    LobbyManager.checkEmail(stringStack[stringStackPointer]);
                    return;
                }
                return;
            }

            if (opcode == CREATE_CONNECTREQUEST) {
                stringStackPointer -= 2;
                intStackPointer -= 2;
                if (MainLogicManager.step != 3) {
                    return;
                }
                if (!LoginManager.inProgress() && LobbyManager.step == 0) {
                    LobbyManager.createAccount(stringStack[stringStackPointer], intStack[intStackPointer], stringStack[stringStackPointer + 1], intStack[intStackPointer + 1] == 1);
                    return;
                }
                return;
            }

            if (opcode == 5606) {
                if (LobbyManager.step == 0) {
                    LobbyManager.response = LobbyManager.LOBBY_RESPONSE_DEFAULT;
                }
                return;
            }

            if (opcode == LOGIN_REPLY) {
                intStack[intStackPointer++] = LoginManager.gameLoginResponse;
                return;
            }

            if (opcode == LOGIN_HOPTIME) {
                intStack[intStackPointer++] = LoginManager.profileTransferTicks;
                return;
            }

            if (opcode == CREATE_REPLY) {
                intStack[intStackPointer++] = LobbyManager.response;
                return;
            }

            if (opcode == LOGIN_DISALLOWRESULT) {
                intStack[intStackPointer++] = LoginManager.disallowResult;
                return;
            }

            if (opcode == LOBBY_ENTERGAME) {
                @Pc(192) int local192 = intStack[--intStackPointer];
                LoginManager.loginToGame(local192);
                return;
            }

            if (opcode == LOBBY_ENTERGAME_REPLY) {
                intStack[intStackPointer++] = LoginManager.gameLoginResponse;
                return;
            }

            if (opcode == LOBBY_ENTERLOBBY) {
                stringStackPointer -= 2;
                @Pc(95) String local95 = stringStack[stringStackPointer];
                @Pc(101) String local101 = stringStack[stringStackPointer + 1];
                LoginManager.enterLobby(local95, local101);
                return;
            }

            if (opcode == LOBBY_LEAVELOBBY) {
                LoginManager.logout(false);
                return;
            }

            if (opcode == LOBBY_ENTERLOBBYREPLY) {
                intStack[intStackPointer++] = LoginManager.lobbyLoginResponse;
                return;
            }

            if (opcode == 5618) {
                intStackPointer--;
                return;
            }

            if (opcode == 5619) {
                intStackPointer--;
                return;
            }

            if (opcode == 5620) {
                intStack[intStackPointer++] = 0;
                return;
            }

            if (opcode == 5621) {
                stringStackPointer -= 2;
                intStackPointer -= 2;
                return;
            }

            if (opcode == 5622) {
                return;
            }

            if (opcode == 5623) {
                if (Client.ssKey != null) {
                    intStack[intStackPointer++] = 1;
                    return;
                }
                intStack[intStackPointer++] = 0;
                return;
            }

            if (opcode == USERFLOW_FLAGS) {
                intStack[intStackPointer++] = (int) (Client.userFlow >> 32);
                intStack[intStackPointer++] = (int) (Client.userFlow & 0xFFFFFFFFFFFFFFFFL);
                return;
            }

            if (opcode == CREATE_UNDER13) {
                intStack[intStackPointer++] = Client.under13 ? 1 : 0;
                return;
            }

            if (opcode == CREATE_SETUNDER13) {
                Client.under13 = true;
                Static358.setUnderageCookie();
                return;
            }

            if (opcode == LOGIN_LAST_TRANSFER_REPLY) {
                intStack[intStackPointer++] = LoginManager.lastGameLoginResponse;
                intStack[intStackPointer++] = LoginManager.lastDisallowResult;
                intStack[intStackPointer++] = LoginManager.lastDisallowTrigger;
                LoginManager.lastGameLoginResponse = -2;
                LoginManager.lastDisallowResult = -1;
                LoginManager.lastDisallowTrigger = -1;
                return;
            }

            if (opcode == LOGIN_INPROGRESS) {
                intStack[intStackPointer++] = LoginManager.inProgress() ? 1 : 0;
                return;
            }

            if (opcode == LOGIN_QUEUE_POSITION) {
                intStack[intStackPointer++] = LoginManager.positionInQueue;
                return;
            }

            if (opcode == LOGIN_CANCEL) {
                LoginManager.cancel();
                return;
            }

            if (opcode == LOGIN_REQUEST_SOCIAL_NETWORK) {
                intStackPointer -= 2;
                @Pc(192) int local192 = intStack[intStackPointer];
                @Pc(834) int local834 = intStack[intStackPointer + 1];
                LoginManager.requestLoginFromSocialNetwork(local192, local834);
                return;
            }

            if (opcode == LOBBY_ENTERLOBBY_SOCIAL_NETWORK) {
                @Pc(192) int local192 = intStack[--intStackPointer];
                LoginManager.loginToLobby(local192);
                return;
            }

            if (opcode == LOGIN_DISALLOWETRIGGER) {
                intStack[intStackPointer++] = LoginManager.disallowTrigger;
                return;
            }
        } else if (opcode < 6100) {
            if (opcode == DETAIL_BRIGHTNESS) {
                @Pc(192) int local192 = intStack[--intStackPointer];
                ClientOptions.instance.update(local192, ClientOptions.instance.brightness);
                MainLogicManager.mapBuild();
                ClientOptions.save();
                Static503.sentPreferences = false;
                return;
            }

            if (opcode == 6002) {
                @Pc(5337) boolean local5337 = intStack[--intStackPointer] == 1;
                ClientOptions.instance.update(local5337 ? 1 : 0, ClientOptions.instance.animateBackgroundDefault);
                ClientOptions.instance.update(local5337 ? 1 : 0, ClientOptions.instance.animateBackground);
                MainLogicManager.mapBuild();
                Static77.method1561();
                ClientOptions.save();
                Static503.sentPreferences = false;
                return;
            }

            if (opcode == DETAIL_REMOVEROOFS_OPTION) {
                @Pc(5337) boolean local5337 = intStack[--intStackPointer] == 1;
                ClientOptions.instance.update(local5337 ? 2 : 1, ClientOptions.instance.removeRoofs);
                ClientOptions.instance.update(local5337 ? 2 : 1, ClientOptions.instance.removeRoofsOverride);
                Static77.method1561();
                ClientOptions.save();
                Static503.sentPreferences = false;
                return;
            }

            if (opcode == DETAIL_GROUNDDECOR_ON) {
                ClientOptions.instance.update(intStack[--intStackPointer] == 1 ? 1 : 0, ClientOptions.instance.groundDecor);
                MainLogicManager.mapBuild();
                ClientOptions.save();
                Static503.sentPreferences = false;
                return;
            }

            if (opcode == DETAIL_IDLEANIMS_MANY) {
                ClientOptions.instance.update(intStack[--intStackPointer], ClientOptions.instance.idleAnimations);
                ClientOptions.save();
                Static503.sentPreferences = false;
                return;
            }

            if (opcode == DETAIL_FLICKERING_ON) {
                ClientOptions.instance.update(intStack[--intStackPointer] == 1 ? 1 : 0, ClientOptions.instance.flickeringEffects);
                ClientOptions.save();
                Static503.sentPreferences = false;
                return;
            }

            if (opcode == DETAIL_SPOTSHADOWS_ON) {
                ClientOptions.instance.update(intStack[--intStackPointer] == 1 ? 1 : 0, ClientOptions.instance.spotShadows);
                ClientOptions.save();
                Static503.sentPreferences = false;
                return;
            }

            if (opcode == DETAIL_HARDSHADOWS) {
                ClientOptions.instance.update(intStack[--intStackPointer], ClientOptions.instance.hardShadows);
                MainLogicManager.mapBuild();
                ClientOptions.save();
                Static503.sentPreferences = false;
                return;
            }

            if (opcode == DETAIL_LIGHTDETAIL_HIGH) {
                ClientOptions.instance.update(intStack[--intStackPointer] == 1 ? 1 : 0, ClientOptions.instance.lightDetail);
                Static296.updateFeatureMask();
                InterfaceManager.loginOpened();
                ClientOptions.save();
                Static503.sentPreferences = false;
                return;
            }

            if (opcode == DETAIL_WATERDETAIL_HIGH) {
                ClientOptions.instance.update(intStack[--intStackPointer] == 1 ? 2 : 0, ClientOptions.instance.waterDetail);
                MainLogicManager.mapBuild();
                ClientOptions.save();
                Static503.sentPreferences = false;
                return;
            }

            if (opcode == DETAIL_FOG_ON) {
                ClientOptions.instance.update(intStack[--intStackPointer] == 1 ? 1 : 0, ClientOptions.instance.fog);
                MainLogicManager.mapBuild();
                ClientOptions.save();
                Static503.sentPreferences = false;
                return;
            }
            if (opcode == DETAIL_ANTIALIASING_QUALITY) {
                ClientOptions.instance.update(intStack[--intStackPointer], ClientOptions.instance.antialiasingQuality);
                Static32.setToolkit(ClientOptions.instance.toolkit.getValue(), false);
                ClientOptions.save();
                return;
            }

            if (opcode == DETAIL_STEREO) {
                ClientOptions.instance.update(intStack[--intStackPointer] == 1 ? 1 : 0, ClientOptions.instance.stereoSound);
                Static150.method2455();
                ClientOptions.save();
                Static503.sentPreferences = false;
                return;
            }

            if (opcode == DETAIL_SOUNDVOL) {
                ClientOptions.instance.update(intStack[--intStackPointer], ClientOptions.instance.soundVolume);
                ClientOptions.save();
                Static503.sentPreferences = false;
                return;
            }

            if (opcode == DETAIL_MUSICVOL) {
                @Pc(192) int local192 = intStack[--intStackPointer];
                @Pc(834) int local834 = ClientOptions.instance.musicVolume.getValue();
                if (local192 != local834) {
                    if (MainLogicStep.isAtGameScreen(MainLogicManager.step)) {
                        if (local834 == 0 && SongManager.playing != -1) {
                            SongManager.method8229(SongManager.playing, local192, js5.MIDI_SONGS);
                            AudioRenderer.mixBussReset();
                            Static501.aBoolean575 = false;
                        } else if (local192 == 0) {
                            Static100.method1988();
                            Static501.aBoolean575 = false;
                        } else {
                            Static126.method2226(local192);
                        }
                    }
                    ClientOptions.instance.update(local192, ClientOptions.instance.musicVolume);
                    ClientOptions.save();
                    Static503.sentPreferences = false;
                }
                return;
            }

            if (opcode == DETAIL_BGSOUNDVOL) {
                ClientOptions.instance.update(intStack[--intStackPointer], ClientOptions.instance.backgroundSoundVolume);
                ClientOptions.save();
                Static503.sentPreferences = false;
                return;
            }

            if (opcode == DETAIL_REMOVEROOFS_OPTION_OVERRIDE) {
                @Pc(192) int local192 = ClientOptions.instance.removeRoofs.getValue();
                ClientOptions.instance.update(intStack[--intStackPointer] == 1 ? 0 : local192, ClientOptions.instance.removeRoofsOverride);
                Static77.method1561();
                return;
            }

            if (opcode == DETAIL_PARTICLES) {
                @Pc(192) int local192 = intStack[--intStackPointer];
                ClientOptions.instance.update(local192, ClientOptions.instance.particles);
                ClientOptions.save();
                Static503.sentPreferences = false;
                return;
            }

            if (opcode == DETAIL_ANTIALIASING_DEFAULT) {
                ClientOptions.instance.update(intStack[--intStackPointer], ClientOptions.instance.antialiasingMode);
                ClientOptions.save();
                return;
            }

            if (opcode == DETAIL_BUILDAREA) {
                ClientOptions.instance.update(intStack[--intStackPointer], ClientOptions.instance.buildArea);
                ClientOptions.save();
                Static503.sentPreferences = false;
                return;
            }

            if (opcode == DETAIL_BLOOM) {
                @Pc(192) int local192 = intStack[--intStackPointer];
                if (local192 < 0 || local192 > 1) {
                    local192 = 0;
                }
                Static249.setBloom(local192 == 1);
                return;
            }

            if (opcode == DETAIL_CUSTOMCURSORS) {
                ClientOptions.instance.update(intStack[--intStackPointer] == 0 ? 0 : 1, ClientOptions.instance.customCursors);
                ClientOptions.save();
                return;
            }

            if (opcode == DETAIL_IDLEANIMS) {
                ClientOptions.instance.update(intStack[--intStackPointer], ClientOptions.instance.idleAnimations);
                ClientOptions.save();
                return;
            }

            if (opcode == DETAIL_GROUNDBLOUNDING) {
                ClientOptions.instance.update(intStack[--intStackPointer] == 0 ? 0 : 1, ClientOptions.instance.groundBlending);
                ClientOptions.save();
                MainLogicManager.mapBuild();
                return;
            }

            if (opcode == DETAIL_TOOLKIT) {
                @Pc(192) int toolkit = intStack[--intStackPointer];
                if (toolkit < ToolkitType.JAVA || toolkit > ToolkitType.GLX) {
                    toolkit = ToolkitType.SSE;
                }
                Static32.setToolkit(toolkit, false);
                return;
            }

            if (opcode == DETAIL_TOOLKIT_DEFAULT) {
                intStackPointer -= 2;
                @Pc(192) int local192 = intStack[intStackPointer];
                @Pc(1578) boolean local1578 = intStack[intStackPointer + 1] == 1;
                ClientOptions.instance.update(local192, ClientOptions.instance.toolkitDefault);
                if (!local1578) {
                    ClientOptions.instance.update(0, ClientOptions.instance.graphicsQuality);
                }
                ClientOptions.save();
                Static503.sentPreferences = false;
                return;
            }

            if (opcode == DETAIL_CPUUSAGE) {
                ClientOptions.instance.update(intStack[--intStackPointer], ClientOptions.instance.cpuUsage);
                ClientOptions.save();
                return;
            }

            if (opcode == DETAIL_TEXTURING) {
                ClientOptions.instance.update(intStack[--intStackPointer] == 1 ? 1 : 0, ClientOptions.instance.textures);
                ClientOptions.save();
                Static296.updateFeatureMask();
                Static503.sentPreferences = false;
                return;
            }

            if (opcode == DETAIL_ANIMDETAIL) {
                @Pc(192) int local192 = ClientOptions.instance.animateBackgroundDefault.getValue();
                ClientOptions.instance.update(intStack[--intStackPointer] == 1 ? 1 : local192, ClientOptions.instance.animateBackground);
                MainLogicManager.mapBuild();
                Static77.method1561();
                return;
            }

            if (opcode == DETAIL_MAXSCREENSIZE) {
                ClientOptions.instance.update(intStack[--intStackPointer], ClientOptions.instance.maxScreenSize);
                ClientOptions.save();
                Client.changingWindowMode = true;
                return;
            }

            if (opcode == DETAIL_SPEECHVOL) {
                ClientOptions.instance.update(intStack[--intStackPointer], ClientOptions.instance.speechVolume);
                ClientOptions.save();
                Static503.sentPreferences = false;
                return;
            }

            if (opcode == DETAIL_LOGINVOL) {
                @Pc(192) int local192 = intStack[--intStackPointer];
                @Pc(834) int local834 = ClientOptions.instance.loginVolume.getValue();
                if (local192 != local834 && SongManager.playing == AudioDefaults.themeMusic) {
                    if (!MainLogicStep.isAtGameScreen(MainLogicManager.step)) {
                        if (local834 == 0) {
                            SongManager.method8229(SongManager.playing, local192, js5.MIDI_SONGS);
                            AudioRenderer.mixBussReset();
                            Static501.aBoolean575 = false;
                        } else if (local192 == 0) {
                            Static100.method1988();
                            Static501.aBoolean575 = false;
                        } else {
                            Static126.method2226(local192);
                        }
                    }
                    ClientOptions.instance.update(local192, ClientOptions.instance.loginVolume);
                    ClientOptions.save();
                    Static503.sentPreferences = false;
                }
                return;
            }

            if (opcode == DETAIL_LOADINGSCREENTYPE) {
                @Pc(192) int local192 = intStack[--intStackPointer];
                if (local192 > 255 || local192 < 0) {
                    local192 = 0;
                }
                if (local192 != ClientOptions.instance.loadingSequence.getValue()) {
                    ClientOptions.instance.update(local192, ClientOptions.instance.loadingSequence);
                    ClientOptions.save();
                    Static503.sentPreferences = false;
                }
                return;
            }

            if (opcode == DETAIL_ORTHOGRAPHIC) {
                @Pc(192) int local192 = intStack[--intStackPointer];
                if (local192 != ClientOptions.instance.orthographic.getValue()) {
                    ClientOptions.instance.update(local192, ClientOptions.instance.orthographic);
                    ClientOptions.save();
                    Static503.sentPreferences = false;
                    OrthoMode.enter();
                }
                return;
            }

            if (opcode == DETAIL_SKYDETAIL) {
                @Pc(192) int local192 = intStack[--intStackPointer];
                if (local192 != ClientOptions.instance.skydetail.getValue()) {
                    ClientOptions.instance.update(local192, ClientOptions.instance.skydetail);
                    ClientOptions.save();
                    Static503.sentPreferences = false;
                }
                return;
            }
        } else if (opcode < 6200) {
            if (opcode == DETAILGET_BRIGHTNESS) {
                intStack[intStackPointer++] = ClientOptions.instance.brightness.getValue();
                return;
            }
            if (opcode == DETAILGET_ANIMDETAIL) {
                intStack[intStackPointer++] = ClientOptions.instance.animateBackgroundDefault.getValue() == 1 ? 1 : 0;
                return;
            }
            if (opcode == DETAILGET_REMOVEROOFS_OPTIONS) {
                intStack[intStackPointer++] = ClientOptions.instance.removeRoofs.getValue() == 2 ? 1 : 0;
                return;
            }
            if (opcode == DETAILGET_GROUNDDECOR_ON) {
                intStack[intStackPointer++] = ClientOptions.instance.groundDecor.getValue() == 1 ? 1 : 0;
                return;
            }
            if (opcode == DETAILGET_IDLEANIMS_MANY) {
                intStack[intStackPointer++] = ClientOptions.instance.idleAnimations.getValue();
                return;
            }
            if (opcode == DETAILGET_FLICKERING_ON) {
                intStack[intStackPointer++] = ClientOptions.instance.flickeringEffects.getValue() == 1 ? 1 : 0;
                return;
            }
            if (opcode == DETAILGET_SPOTSHADOWS) {
                intStack[intStackPointer++] = ClientOptions.instance.spotShadows.getValue() == 1 ? 1 : 0;
                return;
            }
            if (opcode == DETAILGET_HARDSHADOWS) {
                intStack[intStackPointer++] = ClientOptions.instance.hardShadows.getValue();
                return;
            }
            if (opcode == DETAILGET_LIGHTDETAIL_HIGH) {
                intStack[intStackPointer++] = ClientOptions.instance.lightDetail.getValue() == 1 ? 1 : 0;
                return;
            }
            if (opcode == DETAILGET_WATERDETAIL_HIGH) {
                intStack[intStackPointer++] = ClientOptions.instance.waterDetail.getValue() == 2 ? 1 : 0;
                return;
            }
            if (opcode == DETAILGET_FOG_ON) {
                intStack[intStackPointer++] = ClientOptions.instance.fog.getValue() == 1 ? 1 : 0;
                return;
            }
            if (opcode == DETAILGET_ANTIALIASING_QUALITY) {
                intStack[intStackPointer++] = ClientOptions.instance.antialiasingQuality.getValue();
                return;
            }
            if (opcode == DETAILGET_STEREO) {
                intStack[intStackPointer++] = ClientOptions.instance.stereoSound.getValue() == 1 ? 1 : 0;
                return;
            }
            if (opcode == DETAILGET_SOUNDVOL) {
                intStack[intStackPointer++] = ClientOptions.instance.soundVolume.getValue();
                return;
            }
            if (opcode == DETAILGET_MUSICVOL) {
                intStack[intStackPointer++] = ClientOptions.instance.musicVolume.getValue();
                return;
            }
            if (opcode == DETAILGET_BGSOUNDVOL) {
                intStack[intStackPointer++] = ClientOptions.instance.backgroundSoundVolume.getValue();
                return;
            }
            if (opcode == DETAILGET_PARTICLES) {
                intStack[intStackPointer++] = ParticleManager.getOption();
                return;
            }
            if (opcode == DETAILGET_ANTIALIASING) {
                intStack[intStackPointer++] = ClientOptions.instance.antialiasingMode.getValue();
                return;
            }
            if (opcode == DETAILGET_BUILDAREA) {
                intStack[intStackPointer++] = ClientOptions.instance.buildArea.getValue();
                return;
            }
            if (opcode == DETAILGET_BLOOM) {
                intStack[intStackPointer++] = ClientOptions.instance.bloom.getValue() == 1 ? 1 : 0;
                return;
            }
            if (opcode == DETAILGET_CUSTOMCURSORS) {
                intStack[intStackPointer++] = ClientOptions.instance.customCursors.getValue() == 1 ? 1 : 0;
                return;
            }
            if (opcode == DETAILGET_IDLEANIMS) {
                intStack[intStackPointer++] = ClientOptions.instance.idleAnimations.getValue();
                return;
            }
            if (opcode == DETAILGET_GROUNDBLENDING) {
                intStack[intStackPointer++] = ClientOptions.instance.groundBlending.getValue() == 1 ? 1 : 0;
                return;
            }
            if (opcode == DETAILGET_TOOLKIT) {
                intStack[intStackPointer++] = ClientOptions.instance.toolkit.getValue();
                return;
            }
            if (opcode == DETAILGET_TOOLKIT_DEFAULT) {
                intStack[intStackPointer++] = ClientOptions.instance.toolkitDefault.getValue();
                return;
            }
            if (opcode == 6133) {
                intStack[intStackPointer++] = GameShell.signLink.signed && !GameShell.signLink.microsoftjava ? 1 : 0;
                return;
            }
            if (opcode == DETAILGET_CPUUSAGE) {
                intStack[intStackPointer++] = ClientOptions.instance.cpuUsage.value();
                return;
            }
            if (opcode == DETAILGET_TEXTURING) {
                intStack[intStackPointer++] = ClientOptions.instance.textures.getValue() == 1 ? 1 : 0;
                return;
            }
            if (opcode == DETAILGET_PERFORMANCE_METRIC) {
                intStack[intStackPointer++] = Static363.profileToolkit(200, ClientOptions.instance.toolkit.getValue());
                return;
            }
            if (opcode == DETAILGET_MAXSCREENSIZE) {
                intStack[intStackPointer++] = ClientOptions.instance.maxScreenSize.getValue();
                return;
            }
            if (opcode == DETAILGET_SPEECHVOL) {
                intStack[intStackPointer++] = ClientOptions.instance.speechVolume.getValue();
                return;
            }
            if (opcode == DETAILGET_LOGINVOL) {
                intStack[intStackPointer++] = ClientOptions.instance.loginVolume.getValue();
                return;
            }
            if (opcode == DETAILGET_SAFEMODE) {
                intStack[intStackPointer++] = Static3.chooseSafeMode ? 1 : 0;
                return;
            }
            if (opcode == DETAILGET_LOADINGSCREENTYPE) {
                intStack[intStackPointer++] = ClientOptions.instance.loadingSequence.getValue();
                return;
            }
            if (opcode == DETAILGET_ORTHOGRAPHIC) {
                intStack[intStackPointer++] = ClientOptions.instance.orthographic.getValue();
                return;
            }
            if (opcode == DETAILGET_CANCHOOSESAFEMODE) {
                intStack[intStackPointer++] = SystemInfo.instance.totalMemory < 512 || Static3.chooseSafeMode || Static171.graphicsError ? 1 : 0;
                return;
            }
            if (opcode == DETAILGET_CHOSESAFEMODE) {
                intStack[intStackPointer++] = Static416.aBoolean472 ? 1 : 0;
                return;
            }
            if (opcode == DETAILGET_SKYDETAIL) {
                intStack[intStackPointer++] = ClientOptions.instance.skydetail.getValue();
                return;
            }
        } else if (opcode < 6300) {
            if (opcode == VIEWPORT_SETFOV) {
                intStackPointer -= 2;
                Static640.aShort122 = (short) intStack[intStackPointer];
                if (Static640.aShort122 <= 0) {
                    Static640.aShort122 = 256;
                }
                Static640.aShort121 = (short) intStack[intStackPointer + 1];
                if (Static640.aShort121 <= 0) {
                    Static640.aShort121 = 205;
                }
                return;
            }

            if (opcode == VIEWPORT_SETZOOM) {
                intStackPointer -= 2;
                Static228.aShort45 = (short) intStack[intStackPointer];
                if (Static228.aShort45 <= 0) {
                    Static228.aShort45 = 256;
                }
                Camera.zoom = (short) intStack[intStackPointer + 1];
                if (Camera.zoom <= 0) {
                    Camera.zoom = 320;
                }
                return;
            }

            if (opcode == VIEWPORT_CLAMPFOV) {
                intStackPointer -= 4;
                Static25.aShort1 = (short) intStack[intStackPointer];
                if (Static25.aShort1 <= 0) {
                    Static25.aShort1 = 1;
                }
                Static598.aShort120 = (short) intStack[intStackPointer + 1];
                if (Static598.aShort120 <= 0) {
                    Static598.aShort120 = 32767;
                } else if (Static598.aShort120 < Static25.aShort1) {
                    Static598.aShort120 = Static25.aShort1;
                }
                Static552.aShort123 = (short) intStack[intStackPointer + 2];
                if (Static552.aShort123 <= 0) {
                    Static552.aShort123 = 1;
                }
                Static306.aShort59 = (short) intStack[intStackPointer + 3];
                if (Static306.aShort59 <= 0) {
                    Static306.aShort59 = 32767;
                    return;
                }
                if (Static306.aShort59 < Static552.aShort123) {
                    Static306.aShort59 = Static552.aShort123;
                }
                return;
            }

            if (opcode == VIEWPORT_GETEFFECTIVESIZE) {
                Static498.method6643(0, false, 0, InterfaceManager.scene.height, InterfaceManager.scene.width);
                intStack[intStackPointer++] = Static242.anInt3971;
                intStack[intStackPointer++] = Static200.anInt3305;
                return;
            }

            if (opcode == VIEWPORT_GETZOOM) {
                intStack[intStackPointer++] = Static228.aShort45;
                intStack[intStackPointer++] = Camera.zoom;
                return;
            }

            if (opcode == VIEWPORT_GETFOV) {
                intStack[intStackPointer++] = Static640.aShort122;
                intStack[intStackPointer++] = Static640.aShort121;
                return;
            }
        } else if (opcode < 6400) {
            if (opcode == DATE_MINUTES) {
                intStack[intStackPointer++] = (int) (SystemTimer.safetime() / TimeUtils.MILLISECONDS_PER_MINUTE);
                return;
            }

            if (opcode == DATE_RUNEDAY) {
                intStack[intStackPointer++] = (int) (SystemTimer.safetime() / TimeUtils.MILLISECONDS_PER_DAY) - TimeUtils.RUNEDAYS_SINCE_UNIX_EPOCH;
                return;
            }

            if (opcode == DATE_RUNEDAY_FROMDATE) {
                intStackPointer -= 3;
                @Pc(192) int dayOfMonth = intStack[intStackPointer];
                @Pc(834) int month = intStack[intStackPointer + 1];
                @Pc(109) int year = intStack[intStackPointer + 2];
                @Pc(7384) long time = TimeUtils.timeFromDate(dayOfMonth, month, year);
                @Pc(3561) int runedays = TimeUtils.runedayFromTime(time);
                if (year < 1970) {
                    runedays--;
                }
                intStack[intStackPointer++] = runedays;
                return;
            }

            if (opcode == DATE_YEAR) {
                intStack[intStackPointer++] = TimeUtils.yearFromTime(SystemTimer.safetime());
                return;
            }

            if (opcode == DATE_ISLEAPYEAR) {
                @Pc(192) int year = intStack[--intStackPointer];
                @Pc(1578) boolean leapYear = true;
                if (year < 0) {
                    leapYear = (year + 1) % 4 == 0;
                } else if (year < 1582) {
                    leapYear = year % 4 == 0;
                } else if (year % 4 != 0) {
                    leapYear = false;
                } else if (year % 100 != 0) {
                    leapYear = true;
                } else if (year % 400 != 0) {
                    leapYear = false;
                }
                intStack[intStackPointer++] = leapYear ? 1 : 0;
                return;
            }

            if (opcode == DATE_RUNEDAY_TODATE) {
                @Pc(192) int runeday = intStack[--intStackPointer];
                @Pc(7512) int[] date = TimeUtils.dateFromRuneday(runeday);
                Arrays.copy(date, 0, intStack, intStackPointer, 3);
                intStackPointer += 3;
                return;
            }

            if (opcode == DATE_MINUTES_FROMRUNEDAY) {
                @Pc(192) int runeday = intStack[--intStackPointer];
                intStack[intStackPointer++] = (int) (TimeUtils.timeFromRuneday(runeday) / TimeUtils.MILLISECONDS_PER_MINUTE);
                return;
            }
        } else if (opcode < 6500) {
            if (opcode == 6405) {
                intStack[intStackPointer++] = Static21.showVideoAd() ? 1 : 0;
                return;
            }
            if (opcode == 6406) {
                intStack[intStackPointer++] = Static385.showingVideoAd() ? 1 : 0;
                return;
            }
        } else if (opcode < 6600) {
            if (opcode == WORLDLIST_FETCH) {
                if (MainLogicManager.step == MainLogicStep.STEP_LOBBY_SCREEN && !LoginManager.inProgress() && LobbyManager.step == 0) {
                    if (WorldList.fetching) {
                        intStack[intStackPointer++] = 0;
                        return;
                    }
                    if (WorldList.lastReply > SystemTimer.safetime() - 1000L) {
                        intStack[intStackPointer++] = 1;
                        return;
                    }
                    WorldList.fetching = true;
                    @Pc(7662) ClientMessage message = ClientMessage.create(ClientProt.WORLDLIST_FETCH, ServerConnection.LOBBY.isaac);
                    message.bitPacket.p4(WorldList.checksum);
                    ServerConnection.LOBBY.send(message);
                    intStack[intStackPointer++] = 0;
                    return;
                }
                intStack[intStackPointer++] = 1;
                return;
            }

            if (opcode == WORLDLIST_START) {
                @Pc(7686) GameWorld world = WorldList.first();
                if (world != null) {
                    intStack[intStackPointer++] = world.id;
                    intStack[intStackPointer++] = world.flags;
                    stringStack[stringStackPointer++] = world.activity;
                    @Pc(7719) Country country = world.getCountry();
                    intStack[intStackPointer++] = country.flag;
                    stringStack[stringStackPointer++] = country.name;
                    intStack[intStackPointer++] = world.population;
                    intStack[intStackPointer++] = world.ping;
                    stringStack[stringStackPointer++] = world.address;
                    return;
                }

                intStack[intStackPointer++] = -1;
                intStack[intStackPointer++] = 0;
                stringStack[stringStackPointer++] = "";
                intStack[intStackPointer++] = 0;
                stringStack[stringStackPointer++] = "";
                intStack[intStackPointer++] = 0;
                intStack[intStackPointer++] = 0;
                stringStack[stringStackPointer++] = "";
                return;
            }

            if (opcode == WORLDLIST_NEXT) {
                @Pc(7686) GameWorld world = WorldList.next();
                if (world != null) {
                    intStack[intStackPointer++] = world.id;
                    intStack[intStackPointer++] = world.flags;
                    stringStack[stringStackPointer++] = world.activity;
                    @Pc(7719) Country country = world.getCountry();
                    intStack[intStackPointer++] = country.flag;
                    stringStack[stringStackPointer++] = country.name;
                    intStack[intStackPointer++] = world.population;
                    intStack[intStackPointer++] = world.ping;
                    stringStack[stringStackPointer++] = world.address;
                    return;
                }
                intStack[intStackPointer++] = -1;
                intStack[intStackPointer++] = 0;
                stringStack[stringStackPointer++] = "";
                intStack[intStackPointer++] = 0;
                stringStack[stringStackPointer++] = "";
                intStack[intStackPointer++] = 0;
                intStack[intStackPointer++] = 0;
                stringStack[stringStackPointer++] = "";
                return;
            }

            if (opcode == WORLDLIST_SWITCH) {
                @Pc(192) int world = intStack[--intStackPointer];
                @Pc(101) String address = stringStack[--stringStackPointer];
                if (MainLogicManager.step == MainLogicStep.STEP_LOBBY_SCREEN && !LoginManager.inProgress() && LobbyManager.step == 0) {
                    intStack[intStackPointer++] = client.connectTo(world, address) ? 1 : 0;
                    return;
                }
                intStack[intStackPointer++] = 0;
                return;
            }

            if (opcode == WORLDLIST_SPECIFIC) {
                @Pc(192) int id = intStack[--intStackPointer];
                @Pc(8053) GameWorld world = WorldList.list(id);
                if (world != null) {
                    intStack[intStackPointer++] = world.flags;
                    stringStack[stringStackPointer++] = world.activity;
                    @Pc(8077) Country country = world.getCountry();
                    intStack[intStackPointer++] = country.flag;
                    stringStack[stringStackPointer++] = country.name;
                    intStack[intStackPointer++] = world.population;
                    intStack[intStackPointer++] = world.ping;
                    stringStack[stringStackPointer++] = world.address;
                    return;
                }
                intStack[intStackPointer++] = -1;
                stringStack[stringStackPointer++] = "";
                intStack[intStackPointer++] = 0;
                stringStack[stringStackPointer++] = "";
                intStack[intStackPointer++] = 0;
                intStack[intStackPointer++] = 0;
                stringStack[stringStackPointer++] = "";
                return;
            }

            if (opcode == WORLDLIST_SORT) {
                intStackPointer -= 4;
                @Pc(192) int primaryComparison = intStack[intStackPointer];
                @Pc(1578) boolean primaryDescending = intStack[intStackPointer + 1] == 1;
                @Pc(109) int secondaryComparison = intStack[intStackPointer + 2];
                @Pc(2331) boolean secondaryDescending = intStack[intStackPointer + 3] == 1;
                WorldList.quicksortWorldList(primaryComparison, primaryDescending, secondaryComparison, secondaryDescending);
                return;
            }

            if (opcode == WORLDLIST_AUTOWORLD) {
                WorldList.selectAutoWorld();
                return;
            }

            if (opcode == WORLDLIST_PINGWORLDS) {
                if (MainLogicManager.step != 7) {
                    return;
                }
                WorldList.pingWorlds = intStack[--intStackPointer] == 1;
                return;
            }

            if (opcode == WORLDLIST_SPECIFIC_THISWORLD) {
                intStack[intStackPointer++] = Client.worldFlags;
                return;
            }
        } else if (opcode >= 6700) {
            if (opcode < 6800 && Client.modeWhat == ModeWhat.WIP) {
                if (opcode == IF_DEBUG_GETOPENIFCOUNT) {
                    @Pc(192) int count = InterfaceManager.subInterfaces.size();
                    if (InterfaceManager.topLevelInterface != -1) {
                        count++;
                    }
                    intStack[intStackPointer++] = count;
                    return;
                }

                if (opcode == IF_GETTOP) {
                    @Pc(192) int local192 = intStack[--intStackPointer];
                    if (InterfaceManager.topLevelInterface != -1) {
                        if (local192 == 0) {
                            intStack[intStackPointer++] = InterfaceManager.topLevelInterface;
                            return;
                        }
                        local192--;
                    }
                    @Pc(8344) SubInterface sub = (SubInterface) InterfaceManager.subInterfaces.first();
                    while (local192-- > 0) {
                        sub = (SubInterface) InterfaceManager.subInterfaces.next();
                    }
                    intStack[intStackPointer++] = sub.id;
                    return;
                }

                if (opcode == IF_DEBUG_GETNAME) {
                    @Pc(192) int id = intStack[--intStackPointer];
                    if (InterfaceList.interfaces[id] == null) {
                        stringStack[stringStackPointer++] = "";
                        return;
                    }
                    @Pc(101) String name = InterfaceList.interfaces[id][0].name;
                    if (name == null) {
                        stringStack[stringStackPointer++] = "";
                        return;
                    }
                    stringStack[stringStackPointer++] = name.substring(0, name.indexOf(58));
                    return;
                }

                if (opcode == IF_DEBUG_GETCOMCOUNT) {
                    @Pc(192) int id = intStack[--intStackPointer];
                    if (InterfaceList.interfaces[id] == null) {
                        intStack[intStackPointer++] = 0;
                        return;
                    }
                    intStack[intStackPointer++] = InterfaceList.interfaces[id].length;
                    return;
                }

                if (opcode == IF_DEBUG_GETCOMNAME) {
                    intStackPointer -= 2;
                    @Pc(192) int id = intStack[intStackPointer];
                    @Pc(834) int slot = intStack[intStackPointer + 1];
                    if (InterfaceList.interfaces[id] == null) {
                        stringStack[stringStackPointer++] = "";
                        return;
                    }
                    @Pc(198) String name = InterfaceList.interfaces[id][slot].name;
                    if (name == null) {
                        stringStack[stringStackPointer++] = "";
                        return;
                    }
                    stringStack[stringStackPointer++] = name;
                    return;
                }

                if (opcode == IF_DEBUG_GETSERVERTRIGGERS) {
                    intStackPointer -= 2;
                    @Pc(192) int id = intStack[intStackPointer];
                    @Pc(834) int slot = intStack[intStackPointer + 1];
                    if (InterfaceList.interfaces[id] == null) {
                        intStack[intStackPointer++] = 0;
                        return;
                    }
                    intStack[intStackPointer++] = InterfaceList.interfaces[id][slot].serverTriggers;
                    return;
                }

                if (opcode == 6706) {
                    return;
                }

                if (opcode == IF_DEBUG_BUTTON1) {
                    intStackPointer -= 3;
                    @Pc(192) int id = intStack[intStackPointer];
                    @Pc(834) int slot = intStack[intStackPointer + 1];
                    @Pc(109) int component = intStack[intStackPointer + 2];
                    InterfaceManager.ifButtonXSend(id << 16 | slot, component, "", 1);
                    return;
                }

                if (opcode == IF_DEBUG_BUTTON2) {
                    intStackPointer -= 3;
                    @Pc(192) int id = intStack[intStackPointer];
                    @Pc(834) int slot = intStack[intStackPointer + 1];
                    @Pc(109) int component = intStack[intStackPointer + 2];
                    InterfaceManager.ifButtonXSend(id << 16 | slot, component, "", 2);
                    return;
                }

                if (opcode == IF_DEBUG_BUTTON3) {
                    intStackPointer -= 3;
                    @Pc(192) int id = intStack[intStackPointer];
                    @Pc(834) int slot = intStack[intStackPointer + 1];
                    @Pc(109) int component = intStack[intStackPointer + 2];
                    InterfaceManager.ifButtonXSend(id << 16 | slot, component, "", 3);
                    return;
                }

                if (opcode == IF_DEBUG_BUTTON4) {
                    intStackPointer -= 3;
                    @Pc(192) int id = intStack[intStackPointer];
                    @Pc(834) int slot = intStack[intStackPointer + 1];
                    @Pc(109) int component = intStack[intStackPointer + 2];
                    InterfaceManager.ifButtonXSend(id << 16 | slot, component, "", 4);
                    return;
                }

                if (opcode == IF_DEBUG_BUTTON5) {
                    intStackPointer -= 3;
                    @Pc(192) int id = intStack[intStackPointer];
                    @Pc(834) int slot = intStack[intStackPointer + 1];
                    @Pc(109) int component = intStack[intStackPointer + 2];
                    InterfaceManager.ifButtonXSend(id << 16 | slot, component, "", 5);
                    return;
                }

                if (opcode == IF_DEBUG_BUTTON6) {
                    intStackPointer -= 3;
                    @Pc(192) int id = intStack[intStackPointer];
                    @Pc(834) int slot = intStack[intStackPointer + 1];
                    @Pc(109) int component = intStack[intStackPointer + 2];
                    InterfaceManager.ifButtonXSend(id << 16 | slot, component, "", 6);
                    return;
                }

                if (opcode == IF_DEBUG_BUTTON7) {
                    intStackPointer -= 3;
                    @Pc(192) int id = intStack[intStackPointer];
                    @Pc(834) int slot = intStack[intStackPointer + 1];
                    @Pc(109) int component = intStack[intStackPointer + 2];
                    InterfaceManager.ifButtonXSend(id << 16 | slot, component, "", 7);
                    return;
                }

                if (opcode == IF_DEBUG_BUTTON8) {
                    intStackPointer -= 3;
                    @Pc(192) int id = intStack[intStackPointer];
                    @Pc(834) int slot = intStack[intStackPointer + 1];
                    @Pc(109) int component = intStack[intStackPointer + 2];
                    InterfaceManager.ifButtonXSend(id << 16 | slot, component, "", 8);
                    return;
                }

                if (opcode == IF_DEBUG_BUTTON9) {
                    intStackPointer -= 3;
                    @Pc(192) int id = intStack[intStackPointer];
                    @Pc(834) int slot = intStack[intStackPointer + 1];
                    @Pc(109) int component = intStack[intStackPointer + 2];
                    InterfaceManager.ifButtonXSend(id << 16 | slot, component, "", 9);
                    return;
                }

                if (opcode == IF_DEBUG_BUTTON10) {
                    intStackPointer -= 3;
                    @Pc(192) int local192 = intStack[intStackPointer];
                    @Pc(834) int local834 = intStack[intStackPointer + 1];
                    @Pc(109) int local109 = intStack[intStackPointer + 2];
                    InterfaceManager.ifButtonXSend(local192 << 16 | local834, local109, "", 10);
                    return;
                }

                if (opcode == IF_DEBUG_TARGET) {
                    intStackPointer -= 3;
                    @Pc(192) int id = intStack[intStackPointer];
                    @Pc(834) int slot = intStack[intStackPointer + 1];
                    @Pc(109) int component = intStack[intStackPointer + 2];
                    @Pc(8940) Component target = InterfaceList.getComponent(id << 16 | slot, component);
                    InterfaceManager.endTargetMode();
                    @Pc(8945) ServerActiveProperties properties = InterfaceManager.serverActiveProperties(target);
                    InterfaceManager.enterTargetMode(properties.getTargetMask(), target, properties.targetParam);
                    return;
                }
            } else if (opcode < 6900) {
                if (opcode == MEC_TEXT) {
                    @Pc(192) int id = intStack[--intStackPointer];
                    @Pc(8975) MapElementType type = MapElementTypeList.instance.list(id);
                    if (type.text == null) {
                        stringStack[stringStackPointer++] = "";
                        return;
                    }
                    stringStack[stringStackPointer++] = type.text;
                    return;
                }

                if (opcode == MEC_SPRITE) {
                    @Pc(192) int id = intStack[--intStackPointer];
                    @Pc(8975) MapElementType type = MapElementTypeList.instance.list(id);
                    intStack[intStackPointer++] = type.sprite;
                    return;
                }

                if (opcode == MEC_TEXTSIZE) {
                    @Pc(192) int id = intStack[--intStackPointer];
                    @Pc(8975) MapElementType type = MapElementTypeList.instance.list(id);
                    intStack[intStackPointer++] = type.textSize;
                    return;
                }

                if (opcode == MEC_CATEGORY) {
                    @Pc(192) int id = intStack[--intStackPointer];
                    @Pc(8975) MapElementType type = MapElementTypeList.instance.list(id);
                    intStack[intStackPointer++] = type.category;
                    return;
                }

                if (opcode == MEC_PARAM) {
                    intStackPointer -= 2;
                    @Pc(192) int id = intStack[intStackPointer];
                    @Pc(834) int param = intStack[intStackPointer + 1];
                    @Pc(9098) ParamType type = ParamTypeList.instance.list(param);
                    if (type.isString()) {
                        stringStack[stringStackPointer++] = MapElementTypeList.instance.list(id).param(param, type.defaultstr);
                        return;
                    }
                    intStack[intStackPointer++] = MapElementTypeList.instance.list(id).param(type.defaultint, param);
                    return;
                }
            } else if (opcode < 7000) {
                if (opcode == USERDETAIL_QUICKCHAT) {
                    intStack[intStackPointer++] = UserDetail.underage && !UserDetail.parentalChatConsent ? 1 : 0;
                    return;
                }
                if (opcode == USERDETAIL_LOBBY_MEMBERSHIP) {
                    intStack[intStackPointer++] = (int) (Static416.subscriptionExpiration / TimeUtils.MILLISECONDS_PER_MINUTE);
                    intStack[intStackPointer++] = (int) ((Static416.subscriptionExpiration - SystemTimer.safetime() - Static94.remainingSubscription) / TimeUtils.MILLISECONDS_PER_MINUTE);
                    intStack[intStackPointer++] = UserDetail.activeSubscription ? 1 : 0;
                    return;
                }
                if (opcode == USERDETAIL_LOBBY_RECOVERYDAY) {
                    intStack[intStackPointer++] = UserDetail.lobbyRecoveryDay;
                    return;
                }
                if (opcode == USERDETAIL_LOBBY_UNREADMESSAGES) {
                    intStack[intStackPointer++] = UserDetail.lobbyUnreadMessages;
                    return;
                }
                if (opcode == USERDETAIL_LOBBY_LASTLOGINDAY) {
                    intStack[intStackPointer++] = UserDetail.lobbyLastLoginDay;
                    return;
                }
                if (opcode == USERDETAIL_LOBBY_LASTLOGINADDRESS) {
                    @Pc(95) String address = "";
                    if (Static439.hostnameResource != null) {
                        if (Static439.hostnameResource.result == null) {
                            address = WebTools.ipDecode(Static439.hostnameResource.intData1);
                        } else {
                            address = (String) Static439.hostnameResource.result;
                        }
                    }
                    stringStack[stringStackPointer++] = address;
                    return;
                }
                if (opcode == USERDETAIL_LOBBY_EMAILSTATUS) {
                    intStack[intStackPointer++] = UserDetail.lobbyEmailStatus;
                    return;
                }
                if (opcode == USERDETAIL_LOBBY_CCEXPIRY) {
                    intStack[intStackPointer++] = UserDetail.lobbyCCExpiry;
                    return;
                }
                if (opcode == USERDETAIL_LOBBY_GRACEEXPIRY) {
                    intStack[intStackPointer++] = UserDetail.lobbyGraceExpiry;
                    return;
                }
                if (opcode == USERDETAIL_LOBBY_DOBREQUESTED) {
                    intStack[intStackPointer++] = UserDetail.lobbyDOBRequested ? 1 : 0;
                    return;
                }
                if (opcode == UDETAIL_DOB) {
                    intStack[intStackPointer++] = UserDetail.dob;
                    return;
                }
                if (opcode == USERDETAIL_LOBBY_MEMBERSSTATS) {
                    intStack[intStackPointer++] = UserDetail.lobbyMembersStats;
                    return;
                }
                if (opcode == USERDETAIL_LOBBY_PLAYAGE) {
                    intStack[intStackPointer++] = UserDetail.lobbyPlayAge;
                    return;
                }
                if (opcode == USERDETAIL_LOBBY_JCOINS_BALANCE) {
                    intStack[intStackPointer++] = UserDetail.lobbyJcoinsBalance;
                    return;
                }
                if (opcode == USERDETAIL_LOBBY_LOYALTY) {
                    intStack[intStackPointer++] = UserDetail.lobbyLoyalty ? 1 : 0;
                    return;
                }
                if (opcode == USERDETAIL_LOBBY_LOYALTYBALANCE) {
                    intStack[intStackPointer++] = UserDetail.lobbyLoyaltyBalance;
                    return;
                }
            } else if (opcode < 7100) {
                if (opcode == AUTOSETUP_DOSETUP) {
                    @Pc(192) int local192 = Static519.autosetup();
                    intStack[intStackPointer++] = Static165.anInt2810 = ClientOptions.instance.toolkit.getValue();
                    intStack[intStackPointer++] = local192;
                    MainLogicManager.mapBuild();
                    ClientOptions.save();
                    Static503.sentPreferences = false;
                    return;
                }

                if (opcode == AUTOSETUP_SETHIGH) {
                    Static395.method9162();
                    MainLogicManager.mapBuild();
                    ClientOptions.save();
                    Static503.sentPreferences = false;
                    return;
                }

                if (opcode == AUTOSETUP_SETMEDIUM) {
                    Static133.method2316();
                    MainLogicManager.mapBuild();
                    ClientOptions.save();
                    Static503.sentPreferences = false;
                    return;
                }

                if (opcode == AUTOSETUP_SETLOW) {
                    Static75.method6239();
                    MainLogicManager.mapBuild();
                    ClientOptions.save();
                    Static503.sentPreferences = false;
                    return;
                }

                if (opcode == AUTOSETUP_SETMIN) {
                    Static468.method7643();
                    MainLogicManager.mapBuild();
                    ClientOptions.save();
                    Static503.sentPreferences = false;
                    return;
                }

                if (opcode == AUTOSETUP_SETCUSTOM) {
                    ClientOptions.instance.update(0, ClientOptions.instance.graphicsQuality);
                    ClientOptions.save();
                    Static503.sentPreferences = false;
                    return;
                }

                if (opcode == AUTOSETUP_BLACKFLAGLAST) {
                    if (Static165.anInt2810 == 2) {
                        Static449.aBoolean511 = true;
                        return;
                    }
                    if (Static165.anInt2810 == 1) {
                        Static698.aBoolean792 = true;
                        return;
                    }
                    if (Static165.anInt2810 == 3) {
                        Static78.aBoolean139 = true;
                    }
                    return;
                }

                if (opcode == AUTOSETUP_GETLEVEL) {
                    intStack[intStackPointer++] = ClientOptions.instance.graphicsQuality.getValue();
                    return;
                }
            } else if (opcode < 7200) {
                if (opcode == VIDEO_ADVERT_PLAY) {
                    intStackPointer -= 2;
                    @Pc(192) int local192 = intStack[intStackPointer];
                    @Pc(834) int local834 = intStack[intStackPointer + 1];
                    if (local192 != -1) {
                        if (local834 > 255) {
                            local834 = 255;
                        } else if (local834 < 0) {
                            local834 = 0;
                        }
                        VideoTypeList.method6802(false, local192, local834);
                    }
                    return;
                }

                if (opcode == VIDEO_ADVERT_FORCE_REMOVE) {
                    @Pc(192) int local192 = intStack[--intStackPointer];
                    if (local192 != -1) {
                        VideoManager.stop(local192);
                    }
                    return;
                }

                if (opcode == VIDEO_ADVERT_ALLOW_SKIP) {
                    @Pc(192) int local192 = intStack[--intStackPointer];
                    if (local192 != -1) {
                        VideoTypeList.method9267(local192);
                    }
                    return;
                }

                if (opcode == 7103) {
                    intStack[intStackPointer++] = LibraryManager.isNativeLoaded("jagtheora") ? 1 : 0;
                    return;
                }
            } else if (opcode < 7300) {
                if (opcode == DETAILCANMOD_GROUNDDECOR) {
                    intStack[intStackPointer++] = ClientOptions.instance.groundDecor.canMod() ? 1 : 0;
                    return;
                }
                if (opcode == DETAILCANMOD_SPOTSHADOWS) {
                    intStack[intStackPointer++] = ClientOptions.instance.spotShadows.canMod() ? 1 : 0;
                    return;
                }
                if (opcode == DETAILCANMOD_HARDSHADOWS) {
                    intStack[intStackPointer++] = ClientOptions.instance.hardShadows.canMod() ? 1 : 0;
                    return;
                }
                if (opcode == DETAILCANMOD_WATERDETAIL) {
                    intStack[intStackPointer++] = ClientOptions.instance.waterDetail.canMod() ? 1 : 0;
                    return;
                }
                if (opcode == DETAILCANMOD_ANTIALIASING) {
                    intStack[intStackPointer++] = ClientOptions.instance.antialiasingMode.canMod() && Toolkit.active.supportsAntiAliasing() ? 1 : 0;
                    return;
                }
                if (opcode == DETAILCANMOD_PARTICLES) {
                    intStack[intStackPointer++] = ClientOptions.instance.particles.canMod() ? 1 : 0;
                    return;
                }
                if (opcode == DETAILCANMOD_BUILDAREA) {
                    intStack[intStackPointer++] = ClientOptions.instance.buildArea.canMod() ? 1 : 0;
                    return;
                }
                if (opcode == DETAILCANMOD_BLOOM) {
                    intStack[intStackPointer++] = ClientOptions.instance.bloom.canMod() && Toolkit.active.supportsBloom() ? 1 : 0;
                    return;
                }
                if (opcode == DETAILCANMOD_GROUNDBLENDING) {
                    intStack[intStackPointer++] = ClientOptions.instance.groundBlending.canMod() ? 1 : 0;
                    return;
                }
                if (opcode == DETAILCANMOD_TEXTURING) {
                    intStack[intStackPointer++] = ClientOptions.instance.textures.canMod() ? 1 : 0;
                    return;
                }
                if (opcode == DETAILCANMOD_MAXSCREENSIZE) {
                    intStack[intStackPointer++] = ClientOptions.instance.maxScreenSize.canMod() ? 1 : 0;
                    return;
                }
                if (opcode == DETAILCANMOD_FOG) {
                    intStack[intStackPointer++] = ClientOptions.instance.fog.canMod() ? 1 : 0;
                    return;
                }
                if (opcode == DETAILCANMOD_ORTHOGRAPHIC) {
                    intStack[intStackPointer++] = ClientOptions.instance.orthographic.canMod() ? 1 : 0;
                    return;
                }
                if (opcode == DETAILCANMOD_TOOLKIT_DEFAULT) {
                    intStack[intStackPointer++] = ClientOptions.instance.toolkitDefault.canMod() ? 1 : 0;
                    return;
                }
                if (opcode == DETAILCANMOD_SKYDETAIL) {
                    intStack[intStackPointer++] = ClientOptions.instance.skydetail.canMod() ? 1 : 0;
                    return;
                }
            } else if (opcode < 7400) {
                if (opcode == DETAILCANSET_GROUNDDECOR) {
                    @Pc(192) int local192 = intStack[--intStackPointer];
                    intStack[intStackPointer++] = ClientOptions.instance.groundDecor.canSet(local192);
                    return;
                }

                if (opcode == DETAILCANSET_SPOTSHADOWS) {
                    @Pc(192) int local192 = intStack[--intStackPointer];
                    intStack[intStackPointer++] = ClientOptions.instance.spotShadows.canSet(local192);
                    return;
                }

                if (opcode == DETAILCANSET_HARDSHADOWS) {
                    @Pc(192) int local192 = intStack[--intStackPointer];
                    intStack[intStackPointer++] = ClientOptions.instance.hardShadows.canSet(local192);
                    return;
                }

                if (opcode == DETAILCANSET_WATERDETAIL) {
                    @Pc(192) int local192 = intStack[--intStackPointer];
                    intStack[intStackPointer++] = ClientOptions.instance.waterDetail.canSet(local192);
                    return;
                }

                if (opcode == DETAILCANSET_ANTIALIASING) {
                    @Pc(192) int local192 = intStack[--intStackPointer];
                    if (!Toolkit.active.supportsAntiAliasing()) {
                        intStack[intStackPointer++] = 3;
                        return;
                    }
                    intStack[intStackPointer++] = ClientOptions.instance.antialiasingMode.canSet(local192);
                    return;
                }

                if (opcode == DETAILCANSET_PARTICLES) {
                    @Pc(192) int local192 = intStack[--intStackPointer];
                    intStack[intStackPointer++] = ClientOptions.instance.particles.canSet(local192);
                    return;
                }

                if (opcode == DETAILCANSET_BUILDAREA) {
                    @Pc(192) int local192 = intStack[--intStackPointer];
                    intStack[intStackPointer++] = ClientOptions.instance.buildArea.canSet(local192);
                    return;
                }

                if (opcode == DETAILCANSET_BLOOM) {
                    @Pc(192) int local192 = intStack[--intStackPointer];
                    if (!Toolkit.active.supportsBloom()) {
                        intStack[intStackPointer++] = 3;
                        return;
                    }
                    intStack[intStackPointer++] = ClientOptions.instance.bloom.canSet(local192);
                    return;
                }

                if (opcode == DETAILCANSET_GROUNDBLENDING) {
                    @Pc(192) int local192 = intStack[--intStackPointer];
                    intStack[intStackPointer++] = ClientOptions.instance.groundBlending.canSet(local192);
                    return;
                }

                if (opcode == DETAILCANSET_TEXTURING) {
                    @Pc(192) int local192 = intStack[--intStackPointer];
                    intStack[intStackPointer++] = ClientOptions.instance.textures.canSet(local192);
                    return;
                }

                if (opcode == DETAILCANSET_MAXSCREENSIZE) {
                    @Pc(192) int local192 = intStack[--intStackPointer];
                    intStack[intStackPointer++] = ClientOptions.instance.maxScreenSize.canSet(local192);
                    return;
                }

                if (opcode == DETAILCANSET_FOG) {
                    @Pc(192) int local192 = intStack[--intStackPointer];
                    intStack[intStackPointer++] = ClientOptions.instance.fog.canSet(local192);
                    return;
                }

                if (opcode == DETAILCANSET_ORTHOGRAPHIC) {
                    @Pc(192) int local192 = intStack[--intStackPointer];
                    intStack[intStackPointer++] = ClientOptions.instance.orthographic.canSet(local192);
                    return;
                }

                if (opcode == DETAILCANSET_TOOLKIT_DEFAULT) {
                    @Pc(192) int local192 = intStack[--intStackPointer];
                    intStack[intStackPointer++] = ClientOptions.instance.toolkitDefault.canSet(local192);
                    return;
                }

                if (opcode == DETAILCANSET_SKYDETAIL) {
                    @Pc(192) int local192 = intStack[--intStackPointer];
                    intStack[intStackPointer++] = ClientOptions.instance.skydetail.canSet(local192);
                    return;
                }
            }
        }

        throw new IllegalStateException(String.valueOf(opcode));
    }

    @OriginalMember(owner = "client!ou", name = "a", descriptor = "(Lclient!pd;I)V")
    public static void executeHookInner(@OriginalArg(0) HookRequest hook, @OriginalArg(1) int maxOps) {
        @Pc(2) Object[] arguments = hook.arguments;
        @Pc(8) int id = (Integer) arguments[0];
        @Pc(12) ClientScript script = ClientScriptList.list(id);
        if (script == null) {
            return;
        }

        intVars = new int[script.intVarCount];
        @Pc(22) int intCount = 0;

        stringVars = new String[script.stringVarCount];
        @Pc(28) int stringCount = 0;

        longVars = new long[script.longVarCount];
        @Pc(34) int longCount = 0;

        for (@Pc(36) int i = 1; i < arguments.length; i++) {
            if (arguments[i] instanceof Integer) {
                @Pc(48) int v = (Integer) arguments[i];
                if (v == Integer.MIN_VALUE + 1) {
                    v = hook.mouseX;
                }
                if (v == Integer.MIN_VALUE + 2) {
                    v = hook.mouseY;
                }
                if (v == Integer.MIN_VALUE + 3) {
                    v = hook.source == null ? -1 : hook.source.slot;
                }
                if (v == Integer.MIN_VALUE + 4) {
                    v = hook.op;
                }
                if (v == Integer.MIN_VALUE + 5) {
                    v = hook.source == null ? -1 : hook.source.id;
                }
                if (v == Integer.MIN_VALUE + 6) {
                    v = hook.target == null ? -1 : hook.target.slot;
                }
                if (v == Integer.MIN_VALUE + 7) {
                    v = hook.target == null ? -1 : hook.target.id;
                }
                if (v == Integer.MIN_VALUE + 8) {
                    v = hook.keyCode;
                }
                if (v == Integer.MIN_VALUE + 9) {
                    v = hook.keyChar;
                }

                intVars[intCount++] = v;
            } else if (arguments[i] instanceof String) {
                @Pc(154) String s = (String) arguments[i];
                if (s.equals("event_opbase")) {
                    s = hook.opBase;
                }

                stringVars[stringCount++] = s;
            } else if (arguments[i] instanceof Long) {
                @Pc(180) long l = (Long) arguments[i];
                longVars[longCount++] = l;
            }
        }

        lastHookId = hook.id;
        executeScript(script, maxOps);
    }

    @OriginalMember(owner = "client!ou", name = "a", descriptor = "(C)I")
    public static int charIsPrintable(@OriginalArg(0) char c) {
        return StringTools.isPrintable(c) ? 1 : 0;
    }

    @OriginalMember(owner = "client!ou", name = "e", descriptor = "(I)Ljava/lang/String;")
    public static String getClanSettingString(@OriginalArg(0) int arg0) {
        @Pc(9) String local9 = activeClanSettings.getExtraSettingString(Client.modeGame.id << 16 | arg0);
        return local9 == null ? "" : local9;
    }

    @OriginalMember(owner = "client!ou", name = "a", descriptor = "(Ljava/lang/String;I)V")
    public static void sendPublicChat(@OriginalArg(0) String arg0, @OriginalArg(1) int opcode) {
        if (Client.staffModLevel == 0 && (UserDetail.underage && !UserDetail.parentalChatConsent || Static617.quickChatWorld)) {
            return;
        }
        @Pc(18) String local18 = arg0.toLowerCase();
        @Pc(20) byte local20 = 0;
        if (local18.startsWith(LocalisedText.CHATCOL0.localise(0))) {
            local20 = 0;
            arg0 = arg0.substring(LocalisedText.CHATCOL0.localise(0).length());
        } else if (local18.startsWith(LocalisedText.CHATCOL1.localise(0))) {
            local20 = 1;
            arg0 = arg0.substring(LocalisedText.CHATCOL1.localise(0).length());
        } else if (local18.startsWith(LocalisedText.CHATCOL2.localise(0))) {
            local20 = 2;
            arg0 = arg0.substring(LocalisedText.CHATCOL2.localise(0).length());
        } else if (local18.startsWith(LocalisedText.CHATCOL3.localise(0))) {
            local20 = 3;
            arg0 = arg0.substring(LocalisedText.CHATCOL3.localise(0).length());
        } else if (local18.startsWith(LocalisedText.CHATCOL4.localise(0))) {
            local20 = 4;
            arg0 = arg0.substring(LocalisedText.CHATCOL4.localise(0).length());
        } else if (local18.startsWith(LocalisedText.CHATCOL5.localise(0))) {
            local20 = 5;
            arg0 = arg0.substring(LocalisedText.CHATCOL5.localise(0).length());
        } else if (local18.startsWith(LocalisedText.CHATCOL6.localise(0))) {
            local20 = 6;
            arg0 = arg0.substring(LocalisedText.CHATCOL6.localise(0).length());
        } else if (local18.startsWith(LocalisedText.CHATCOL7.localise(0))) {
            local20 = 7;
            arg0 = arg0.substring(LocalisedText.CHATCOL7.localise(0).length());
        } else if (local18.startsWith(LocalisedText.CHATCOL8.localise(0))) {
            local20 = 8;
            arg0 = arg0.substring(LocalisedText.CHATCOL8.localise(0).length());
        } else if (local18.startsWith(LocalisedText.CHATCOL9.localise(0))) {
            local20 = 9;
            arg0 = arg0.substring(LocalisedText.CHATCOL9.localise(0).length());
        } else if (local18.startsWith(LocalisedText.CHATCOL10.localise(0))) {
            local20 = 10;
            arg0 = arg0.substring(LocalisedText.CHATCOL10.localise(0).length());
        } else if (local18.startsWith(LocalisedText.CHATCOL11.localise(0))) {
            local20 = 11;
            arg0 = arg0.substring(LocalisedText.CHATCOL11.localise(0).length());
        } else if (Client.language != 0) {
            if (local18.startsWith(LocalisedText.CHATCOL0.localise(Client.language))) {
                local20 = 0;
                arg0 = arg0.substring(LocalisedText.CHATCOL0.localise(Client.language).length());
            } else if (local18.startsWith(LocalisedText.CHATCOL1.localise(Client.language))) {
                local20 = 1;
                arg0 = arg0.substring(LocalisedText.CHATCOL1.localise(Client.language).length());
            } else if (local18.startsWith(LocalisedText.CHATCOL2.localise(Client.language))) {
                local20 = 2;
                arg0 = arg0.substring(LocalisedText.CHATCOL2.localise(Client.language).length());
            } else if (local18.startsWith(LocalisedText.CHATCOL3.localise(Client.language))) {
                local20 = 3;
                arg0 = arg0.substring(LocalisedText.CHATCOL3.localise(Client.language).length());
            } else if (local18.startsWith(LocalisedText.CHATCOL4.localise(Client.language))) {
                local20 = 4;
                arg0 = arg0.substring(LocalisedText.CHATCOL4.localise(Client.language).length());
            } else if (local18.startsWith(LocalisedText.CHATCOL5.localise(Client.language))) {
                local20 = 5;
                arg0 = arg0.substring(LocalisedText.CHATCOL5.localise(Client.language).length());
            } else if (local18.startsWith(LocalisedText.CHATCOL6.localise(Client.language))) {
                local20 = 6;
                arg0 = arg0.substring(LocalisedText.CHATCOL6.localise(Client.language).length());
            } else if (local18.startsWith(LocalisedText.CHATCOL7.localise(Client.language))) {
                local20 = 7;
                arg0 = arg0.substring(LocalisedText.CHATCOL7.localise(Client.language).length());
            } else if (local18.startsWith(LocalisedText.CHATCOL8.localise(Client.language))) {
                local20 = 8;
                arg0 = arg0.substring(LocalisedText.CHATCOL8.localise(Client.language).length());
            } else if (local18.startsWith(LocalisedText.CHATCOL9.localise(Client.language))) {
                local20 = 9;
                arg0 = arg0.substring(LocalisedText.CHATCOL9.localise(Client.language).length());
            } else if (local18.startsWith(LocalisedText.CHATCOL10.localise(Client.language))) {
                local20 = 10;
                arg0 = arg0.substring(LocalisedText.CHATCOL10.localise(Client.language).length());
            } else if (local18.startsWith(LocalisedText.CHATCOL11.localise(Client.language))) {
                local20 = 11;
                arg0 = arg0.substring(LocalisedText.CHATCOL11.localise(Client.language).length());
            }
        }
        local18 = arg0.toLowerCase();
        @Pc(460) byte local460 = 0;
        if (local18.startsWith(LocalisedText.CHATEFFECT1.localise(0))) {
            local460 = 1;
            arg0 = arg0.substring(LocalisedText.CHATEFFECT1.localise(0).length());
        } else if (local18.startsWith(LocalisedText.CHATEFFECT2.localise(0))) {
            local460 = 2;
            arg0 = arg0.substring(LocalisedText.CHATEFFECT2.localise(0).length());
        } else if (local18.startsWith(LocalisedText.CHATEFFECT3.localise(0))) {
            local460 = 3;
            arg0 = arg0.substring(LocalisedText.CHATEFFECT3.localise(0).length());
        } else if (local18.startsWith(LocalisedText.CHATEFFECT4.localise(0))) {
            local460 = 4;
            arg0 = arg0.substring(LocalisedText.CHATEFFECT4.localise(0).length());
        } else if (local18.startsWith(LocalisedText.CHATEFFECT5.localise(0))) {
            local460 = 5;
            arg0 = arg0.substring(LocalisedText.CHATEFFECT5.localise(0).length());
        } else if (Client.language != 0) {
            if (local18.startsWith(LocalisedText.CHATEFFECT1.localise(Client.language))) {
                local460 = 1;
                arg0 = arg0.substring(LocalisedText.CHATEFFECT1.localise(Client.language).length());
            } else if (local18.startsWith(LocalisedText.CHATEFFECT2.localise(Client.language))) {
                local460 = 2;
                arg0 = arg0.substring(LocalisedText.CHATEFFECT2.localise(Client.language).length());
            } else if (local18.startsWith(LocalisedText.CHATEFFECT3.localise(Client.language))) {
                local460 = 3;
                arg0 = arg0.substring(LocalisedText.CHATEFFECT3.localise(Client.language).length());
            } else if (local18.startsWith(LocalisedText.CHATEFFECT4.localise(Client.language))) {
                local460 = 4;
                arg0 = arg0.substring(LocalisedText.CHATEFFECT4.localise(Client.language).length());
            } else if (local18.startsWith(LocalisedText.CHATEFFECT5.localise(Client.language))) {
                local460 = 5;
                arg0 = arg0.substring(LocalisedText.CHATEFFECT5.localise(Client.language).length());
            }
        }
        @Pc(650) ServerConnection local650 = ConnectionManager.active();
        @Pc(656) ClientMessage local656 = ClientMessage.create(ClientProt.MESSAGE_PUBLIC, local650.isaac);
        local656.bitPacket.p1(0);
        @Pc(665) int local665 = local656.bitPacket.pos;
        local656.bitPacket.p1(local20);
        local656.bitPacket.p1(local460);
        WordPack.encode(local656.bitPacket, arg0);
        local656.bitPacket.psize1(local656.bitPacket.pos - local665);
        local650.send(local656);
    }

    @OriginalMember(owner = "client!ou", name = "a", descriptor = "(ILjava/lang/String;I)V")
    public static void executeCutsceneSubtitleTrigger(@OriginalArg(0) int id, @OriginalArg(1) String arg1, @OriginalArg(2) int arg2) {
        @Pc(5) ClientScript script = ClientScriptList.trigger(ClientTriggerType.CUTSCENE_SUBTITLE, id, -1);
        if (script == null) {
            return;
        }
        intVars = new int[script.intVarCount];
        stringVars = new String[script.stringVarCount];
        stringVars[0] = arg1;
        intVars[0] = arg2;
        executeScript(script, 200000);
    }

    @OriginalMember(owner = "client!ou", name = "a", descriptor = "()V")
    public static void profileClear() {
    }

    @OriginalMember(owner = "client!ou", name = "a", descriptor = "(Lclient!mia;II)V")
    public static void executeTrigger(@OriginalArg(0) ClientTriggerType triggerType, @OriginalArg(1) int v1, @OriginalArg(2) int v2) {
        @Pc(5) ClientScript local5 = ClientScriptList.trigger(triggerType, v1, v2);
        if (local5 == null) {
            return;
        }
        intVars = new int[local5.intVarCount];
        stringVars = new String[local5.stringVarCount];
        if (local5.triggerType == ClientTriggerType.MAP_ELEMENT_MOUSEOVER || local5.triggerType == ClientTriggerType.MAP_ELEMENT_MOUSEREPEAT || local5.triggerType == ClientTriggerType.MAP_ELEMENT_MOUSELEAVE) {
            @Pc(35) int local35 = 0;
            @Pc(37) int local37 = 0;
            if (WorldMap.component != null) {
                local35 = WorldMap.component.x;
                local37 = WorldMap.component.y;
            }
            intVars[0] = MouseMonitor.instance.getRecordedX() - local35;
            intVars[1] = MouseMonitor.instance.getRecordedY() - local37;
        }
        executeScript(local5, 200000);
    }

    @OriginalMember(owner = "client!sda", name = "a", descriptor = "(BIII)V")
    public static void executeMapElementTrigger(@OriginalArg(1) int arg0, @OriginalArg(2) int arg1, @OriginalArg(3) int arg2) {
        if (arg2 == MiniMenuAction.OP_MAPELEMENT1) {
            executeTrigger(ClientTriggerType.OP_MAPELEMENT1, arg1, arg0);
        } else if (arg2 == MiniMenuAction.OP_MAPELEMENT2) {
            executeTrigger(ClientTriggerType.OP_MAPELEMENT2, arg1, arg0);
        } else if (arg2 == MiniMenuAction.OP_MAPELEMENT3) {
            executeTrigger(ClientTriggerType.OP_MAPELEMENT3, arg1, arg0);
        } else if (arg2 == MiniMenuAction.OP_MAPELEMENT4) {
            executeTrigger(ClientTriggerType.OP_MAPELEMENT4, arg1, arg0);
        } else if (arg2 == MiniMenuAction.OP_MAPELEMENT5) {
            executeTrigger(ClientTriggerType.OP_MAPELEMENT5, arg1, arg0);
        }
    }
}
