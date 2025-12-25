package com.jagex.core.constants;

public final class ClientScriptOpCode {

    public static final int PUSH_CONSTANT_INT = 0;

    public static final int PUSH_VAR = 1;

    public static final int POP_VAR = 2;

    public static final int PUSH_CONSTANT_STRING = 3;

    public static final int BRANCH = 6;

    public static final int BRANCH_NOT = 7;

    public static final int BRANCH_EQUALS = 8;

    public static final int BRANCH_LESS_THAN = 9;

    public static final int BRANCH_GREATER_THAN = 10;

    public static final int RETURN = 21;

    public static final int POP_VARBIT = 25;

    public static final int PUSH_VARBIT = 27;

    public static final int BRANCH_LESS_THAN_OR_EQUALS = 31;

    public static final int BRANCH_GREATER_THAN_OR_EQUALS = 32;

    public static final int PUSH_INT_LOCAL = 33;

    public static final int POP_INT_LOCAL = 34;

    public static final int PUSH_STRING_LOCAL = 35;

    public static final int POP_STRING_LOCAL = 36;

    public static final int JOIN_STRING = 37;

    public static final int POP_INT_DISCARD = 38;

    public static final int POP_STRING_DISCARD = 39;

    public static final int GOSUB_WITH_PARAMS = 40;

    public static final int PUSH_VARC = 42;

    public static final int POP_VARC = 43;

    public static final int DEFINE_ARRAY = 44;

    public static final int PUSH_ARRAY_INT = 45;

    public static final int POP_ARRAY_INT = 46;

    public static final int PUSH_VARCSTR = 47;

    public static final int POP_VARCSTR = 48;

    public static final int SWITCH = 51;

    public static final int PUSH_LONG_CONSTANT = 54;

    public static final int POP_LONG_DISCARD = 55;

    public static final int PUSH_LONG_LOCAL = 66;

    public static final int POP_LONG_LOCAL = 67;

    public static final int LONG_BRANCH_NOT = 68;

    public static final int LONG_BRANCH_EQUALS = 69;

    public static final int LONG_BRANCH_LESS_THAN = 70;

    public static final int LONG_BRANCH_GREATER_THAN = 71;

    public static final int LONG_BRANCH_LESS_THAN_OR_EQUALS = 72;

    public static final int LONG_BRANCH_GREATER_THAN_OR_EQUALS = 73;

    public static final int BRANCH_IF_TRUE = 86;

    public static final int BRANCH_IF_FALSE = 87;
    
    public static final int PUSH_VARCLAN = 106;

    public static final int PUSH_VARCLANBIT = 107;

    public static final int PUSH_VARCLAN_LONG = 108;

    public static final int PUSH_VARCLAN_STRING = 109;

    public static final int PUSH_VARCLANSETTING = 112;

    public static final int PUSH_VARCLANSETTINGBIT = 113;

    public static final int PUSH_VARCLAN_SETTING_LONG = 114;

    public static final int PUSH_VARCLANSETTING_STRING = 115;

    public static final int CC_CREATE = 150;

    public static final int CC_DELETE = 151;

    public static final int CC_DELETEALL = 152;

    public static final int CC_FIND = 200;

    public static final int IF_FIND = 201;

    public static final int IF_SENDTOFRONT = 202;

    public static final int CC_SENDTOFRONT = 204;

    public static final int IF_SENDTOBACK = 203;

    public static final int CC_SENDTOBACK = 205;

    public static final int BASEIDKIT = 403;

    public static final int BASECOLOUR = 404;

    public static final int SETGENDER = 410;

    public static final int SETOBJ = 411;

    public static final int CC_IF_SETPOSITION = 1000;

    public static final int CC_IF_SETSIZE = 1001;

    public static final int CC_IF_SETHIDE = 1003;

    public static final int CC_IF_SETASPECT = 1004;

    public static final int CC_IF_SETNOCLICKTHROUGH = 1005;

    public static final int CC_IF_SETSCROLLPOS = 1100;

    public static final int CC_IF_SETCOLOUR = 1101;

    public static final int CC_IF_SETFILL = 1102;

    public static final int CC_IF_SETTTRANS = 1103;

    public static final int CC_IF_SETLINEWID = 1104;

    public static final int CC_IF_SETGRAPHIC = 1105;

    public static final int CC_IF_SET2DANGLE = 1106;

    public static final int CC_IF_SETTILING = 1107;

    public static final int CC_IF_SETMODEL = 1108;

    public static final int CC_IF_SETMODELANGLE = 1109;

    public static final int CC_IF_SETMODELANIM = 1110;

    public static final int CC_IF_SETMODELORTHOG = 1111;

    public static final int CC_IF_SETTEXT = 1112;

    public static final int CC_IF_SETTEXTFONT = 1113;

    public static final int CC_IF_SETTEXTALIGN = 1114;

    public static final int CC_IF_SETTEXTSHADOW = 1115;

    public static final int CC_IF_SETOUTLINE = 1116;

    public static final int CC_IF_SETGRAPHICSHADOW = 1117;

    public static final int CC_IF_SETHFLIP = 1118;

    public static final int CC_IF_SETVFLIP = 1119;

    public static final int CC_IF_SETSCROLLSIZE = 1120;

    public static final int CC_IF_SETALPHA = 1122;

    public static final int CC_IF_SETMODELZOOM = 1123;

    public static final int CC_IF_SETLINEDIRECTION = 1124;

    public static final int CC_IF_SETMODELORIGIN = 1125;

    public static final int CC_IF_SETMAXLINES = 1126;

    public static final int CC_IF_SETPARAM_INT = 1127;

    public static final int CC_IF_SETPARAM_STRING = 1128;

    public static final int CC_IF_SETRECOL = 1131;

    public static final int CC_IF_SETRETEX = 1132;

    public static final int CC_IF_SETFONTMONO = 1133;

    public static final int CC_IF_SETPARAM = 1134;

    public static final int CC_IF_SETCLICKMASK = 1135;

    public static final int CC_IF_SETOBJECT = 1200;

    public static final int CC_IF_SETOBJECT_NONUM = 1205;

    public static final int CC_IF_SETOBJECT_WEARCOL = 1208;

    public static final int CC_IF_SETOBJECT_WEARCOL_NONUM = 1209;

    public static final int CC_IF_SETOBJECT_ALWAYSNUM = 1212;

    public static final int CC_IF_SETOBJECT_WEARCOL_ALWAYSNUM = 1213;

    public static final int CC_IF_SETNPCHEAD = 1201;

    public static final int CC_IF_SETPLAYERHEAD_SELF = 1202;

    public static final int CC_IF_SETNPCMODEL = 1203;

    public static final int CC_IF_SETPLAYERMODEL = 1204;

    public static final int CC_IF_SETOBJECT_DATA = 1210;

    public static final int CC_IF_SETPLAYERMODEL_SELF = 1211;

    public static final int CC_IF_SETOP = 1300;

    public static final int CC_IF_SETDRAGGABLE = 1301;

    public static final int CC_IF_SETDRAGRENDERBEHAVIOUR = 1302;

    public static final int CC_IF_SETDRAGDEADZONE = 1303;

    public static final int CC_IF_SETDRAGDEADTIME = 1304;

    public static final int CC_IF_SETOPBASE = 1305;

    public static final int CC_IF_SETTARGETVERB = 1306;

    public static final int CC_IF_CLEAROPS = 1307;

    public static final int CC_IF_SETTARGETCURSORS = 1308;

    public static final int CC_IF_SETOPCURSOR = 1309;

    public static final int CC_IF_SETPAUSETEXT = 1310;

    public static final int CC_IF_SETTARGETOPCURSOR = 1311;

    public static final int CC_IF_SETOPCHAR = 1312;

    public static final int CC_IF_SETOPKEY = 1313;

    public static final int CC_IF_SETMOUSEOVERCURSOR = 1314;

    public static final int CC_IF_CLEARSCRIPTHOOKS = 1499;

    public static final int CC_IF_SETONCLICK = 1400;

    public static final int CC_IF_SETONHOLD = 1401;

    public static final int CC_IF_SETONRELEASE = 1402;

    public static final int CC_IF_SETONMOUSEOVER = 1403;

    public static final int CC_IF_SETONMOUSELEAVE = 1404;

    public static final int CC_IF_SETONDRAG = 1405;

    public static final int CC_IF_SETONTARGETLEAVE = 1406;

    public static final int CC_IF_SETONVARTRANSMIT = 1407;

    public static final int CC_IF_SETONTIMER = 1408;

    public static final int CC_IF_SETONOP = 1409;

    public static final int CC_IF_SETONDRAGCOMPLETE = 1410;

    public static final int CC_IF_SETONCLICKREPEAT = 1411;

    public static final int CC_IF_SETONMOUSEREPEAT = 1412;

    public static final int CC_IF_SETONINVTRANSMIT = 1414;

    public static final int CC_IF_SETONSTATTRANSMIT = 1415;

    public static final int CC_IF_SETONTARGETENTER = 1416;

    public static final int CC_IF_SETONSCROLLWHEEL = 1417;

    public static final int CC_IF_SETONCHATTRANSMIT = 1418;

    public static final int CC_IF_SETONKEY = 1419;

    public static final int CC_IF_SETONFRIENDTRANSMIT = 1420;

    public static final int CC_IF_SETONCLANTRANSMIT = 1421;

    public static final int CC_IF_SETONMISCTRANSMIT = 1422;

    public static final int CC_IF_SETONDIALOGABORT = 1423;

    public static final int CC_IF_SETONSUBCHANGE = 1424;

    public static final int CC_IF_SETONSTOCKTRANSMIT = 1425;

    public static final int CC_IF_SETONCAMFINISHED = 1426;

    public static final int CC_IF_SETONRESIZE = 1427;

    public static final int CC_IF_SETONVARCTRANSMIT = 1428;

    public static final int CC_IF_SETONVARCSTRTRANSMIT = 1429;

    public static final int CC_IF_SETONOPT = 1430;

    public static final int CC_IF_SETONCLANSETTINGSTRANSMIT = 1431;

    public static final int CC_IF_SETONCLANCHANNELTRANSMIT = 1432;

    public static final int CC_IF_SETONVARCLANTRANSMIT = 1433;

    public static final int CC_GETX = 1500;

    public static final int CC_GETY = 1501;

    public static final int CC_GETWIDTH = 1502;

    public static final int CC_GETHEIGHT = 1503;

    public static final int CC_GETHIDE = 1504;

    public static final int CC_GETLAYER = 1505;

    public static final int CC_GETPARENTLAYER = 1506;

    public static final int CC_GETCOLOUR = 1507;

    public static final int CC_GETSCROLLX = 1600;

    public static final int CC_GETSCROLLY = 1601;

    public static final int CC_GETTEXT = 1602;

    public static final int CC_GETSCROLLWIDTH = 1603;

    public static final int CC_GETSCROLLHEIGHT = 1604;

    public static final int CC_GETMODELZOOM = 1605;

    public static final int CC_GETMODELANGLE_X = 1606;

    public static final int CC_GETMODELANGLE_Z = 1607;

    public static final int CC_GETMODELANGLE_Y = 1608;

    public static final int CC_GETTRANS = 1609;

    public static final int CC_GETMODELXOF = 1610;

    public static final int CC_GETMODELYOF = 1611;

    public static final int CC_GETGRAPHIC = 1612;

    public static final int CC_PARAM = 1613;

    public static final int CC_GET2DANGLE = 1614;

    public static final int CC_GETMODEL = 2614;

    public static final int CC_GETFONTGRAPHIC = 1618;

    public static final int CC_GETINVOBJECT = 1700;

    public static final int CC_GETINVCOUNT = 1701;

    public static final int CC_GETID = 1702;

    public static final int CC_GETTARGETMASK = 1800;

    public static final int CC_GETOP = 1801;

    public static final int CC_GETOPBASE = 1802;

    public static final int CC_IF_CALLONRESIZE = 1927;

    public static final int IF_GETX = 2500;

    public static final int IF_GETY = 2501;

    public static final int IF_GETWIDTH = 2502;

    public static final int IF_GETHEIGHT = 2503;

    public static final int IF_GETHIDE = 2504;

    public static final int IF_GETLAYER = 2505;

    public static final int IF_GETPARENTLAYER = 2506;

    public static final int IF_GETCOLOUR = 2507;

    public static final int IF_GETSCROLLX = 2600;

    public static final int IF_GETSCROLLY = 2601;

    public static final int IF_GETTEXT = 2602;

    public static final int IF_GETSCROLLWIDTH = 2603;

    public static final int IF_GETSCROLLHEIGHT = 2604;

    public static final int IF_GETMODELZOOM = 2605;

    public static final int IF_GETMODELANGLE_X = 2606;

    public static final int IF_GETMODELANGLE_Z = 2607;

    public static final int IF_GETMODELANGLE_Y = 2608;

    public static final int IF_GETTRANS = 2609;

    public static final int IF_GETMODELXOF = 2610;

    public static final int IF_GETMODELYOF = 2611;

    public static final int IF_GETGRAPHIC = 2612;

    public static final int IF_GET2DANGLE = 2613;

    public static final int IF_GETMODEL = 2614;

    public static final int IF_GETFONTGRAPHIC = 2617;

    public static final int IF_GETINVOBJECT = 2700;

    public static final int IF_GETINVCOUNT = 2701;

    public static final int IF_HASSUB = 2702;

    public static final int IF_GETNEXTSUBID = 2703;

    public static final int IF_HASSUBMODAL = 2704;

    public static final int IF_HASSUBOVERLAY = 2705;

    public static final int IF_GETTARGETMASK = 2800;

    public static final int IF_GETOP = 2801;

    public static final int IF_GETOPBASE = 2802;

    public static final int MES = 3100;

    public static final int IF_CLOSE = 3103;

    public static final int RESUME_COUNTDIALOG = 3104;

    public static final int RESUME_STRINGDIALOG = 3105;

    public static final int RESUME_NAMEDIALOG = 3106;

    public static final int OPPLAYER = 3107;

    public static final int IF_DRAGPICKUP = 3108;

    public static final int CC_DRAGPICKUP = 3109;

    public static final int RESUME_OBJDIALOG = 3110;

    public static final int IF_OPENSUBCLIENT = 3111;

    public static final int IF_CLOSESUBCLIENT = 3112;

    public static final int OPPLAYERT = 3113;

    public static final int MES_TYPED = 3114;

    public static final int SETUP_MESSAGEBOX = 3115;

    public static final int RESUME_HSLDIALOG = 3116;

    public static final int RESUME_CLANFORUMQFCDIALOG = 3117;

    public static final int SOUND_SYNTH = 3200;

    public static final int SOUND_SONG = 3201;

    public static final int SOUND_JINGLE = 3202;

    public static final int SOUND_SYTHN_VOLUME = 3203;

    public static final int SOUND_SONG_VOLUME = 3204;

    public static final int SOUND_JINGLE_VOLUME = 3205;

    public static final int SOUND_VORBIS_VOLUME = 3206;

    public static final int SOUND_SPEECH_VOLUME = 3207;

    public static final int SOUND_SYNTH_RATE = 3208;

    public static final int SOUND_VORBIS_RATE = 3209;

    public static final int CLIENTCLOCK = 3300;

    public static final int INV_GETOBJ = 3301;

    public static final int INV_GETNUM = 3302;

    public static final int INV_TOTAL = 3303;

    public static final int INV_SIZE = 3304;

    public static final int STAT = 3305;

    public static final int STAT_BASE = 3306;

    public static final int STAT_VISIBLE_XP = 3307;

    public static final int COORD = 3308;

    public static final int COORDX = 3309;

    public static final int COORDY = 3310;

    public static final int COORDZ = 3311;

    public static final int MAP_MEMBERS = 3312;

    public static final int INVOTHER_GETOBJ = 3313;

    public static final int INVOTHER_GETNUM = 3314;

    public static final int INVOTHER_TOTAL = 3315;

    public static final int STAFFMODLEVEL = 3316;

    public static final int REBOOTTIMER = 3317;

    public static final int MAP_WORLD = 3318;

    public static final int RUNENERGY_VISIBLE = 3321;

    public static final int RUNWEIGHT_VISIBLE = 3322;

    public static final int PLAYERMOD = 3323;

    public static final int PLAYERMODLEVEL = 3324;

    public static final int PLAYERMEMBER = 3325;

    public static final int COMLEVEL_ACTIVE = 3326;

    public static final int GENDER = 3327;

    public static final int MAP_QUICKCHAT = 3329;

    public static final int INV_FREESPACE = 3330;

    public static final int INV_TOTALPARAM = 3331;

    public static final int INV_TOTALPARAM_STACK = 3332;

    public static final int MAP_LANG = 3335;

    public static final int MOVE_COORD = 3336;

    public static final int AFFILIATE = 3337;

    public static final int PROFILE_CPU = 3338;

    public static final int PLAYERDEMO = 3339;

    public static final int APPLET_HASFOCUS = 3340;

    public static final int FROMBILLING = 3341;

    public static final int GET_MOUSEX = 3342;

    public static final int GET_MOUSEY = 3343;

    public static final int GET_ACTIVE_MINIMENU_ENTRY = 3344;

    public static final int GET_SECOND_MINIMENU_ENTRY = 3345;

    public static final int GET_MINIMENU_LENGTH = 3346;

    public static final int GET_CURRENTCURSOR = 3347;

    public static final int GET_SELFYANGLE = 3349;

    public static final int MAP_ISOWNER = 3350;

    public static final int GET_MOUSEBUTTONS = 3351;

    public static final int ENUM_STRING = 3400;

    public static final int ENUM = 3408;

    public static final int ENUM_HASOUTPUT = 3409;

    public static final int ENUM_HASOUTPUT_STRING = 3410;

    public static final int ENUM_GETOUTPUT_COUNT = 3411;

    public static final int ENUM_GETREVERSECOUNT = 3412;

    public static final int ENUM_GETREVERSECOUNT_STRING = 3413;

    public static final int ENUM_GETREVERSEINDEX = 3414;

    public static final int ENUM_GETREVERSEINDEX_STRING = 3415;

    public static final int FRIEND_COUNT = 3600;

    public static final int FRIEND_GETNAME = 3601;

    public static final int FRIEND_GETWORLD = 3602;

    public static final int FRIEND_GETRANK = 3603;

    public static final int FRIEND_SETRANK = 3604;

    public static final int FRIEND_ADD = 3605;

    public static final int FRIEND_DEL = 3606;

    public static final int IGNORE_ADD = 3607;

    public static final int IGNORE_DEL = 3608;

    public static final int FRIEND_TEST = 3609;

    public static final int FRIEND_GETWORLDNAME = 3610;

    public static final int CLAN_GETCHATDISPLAYNAME = 3611;

    public static final int CLAN_GETCHATCOUNT = 3612;

    public static final int CLAN_GETCHATUSERNAME = 3613;

    public static final int CLAN_GETCHATUSERWORLD = 3614;

    public static final int CLAN_GETCHATUSERRANK = 3615;

    public static final int CLAN_GETCHATMINKICK = 3616;

    public static final int CLAN_KICKUSER = 3617;

    public static final int CLAN_GETCHATRANK = 3618;

    public static final int CLAN_JOINCHAT = 3619;

    public static final int CLAN_LEAVECHAT = 3620;

    public static final int IGNORE_COUNT = 3621;

    public static final int IGNORE_GETNAME = 3622;

    public static final int IGNORE_TEXT = 3623;

    public static final int CLAN_ISSELF = 3624;

    public static final int CLAN_GETCHATOWNERNAME = 3625;

    public static final int CLAN_GETCHATUSERWORLDNAME = 3626;

    public static final int FRIEND_SAME_GAME = 3627;

    public static final int FRIEND_GETSLOTFROMNAME = 3628;

    public static final int PLAYERCOUNTRY = 3629;

    public static final int IGNORE_ADD_TEMP = 3630;

    public static final int IGNORE_IS_TEMP = 3631;

    public static final int CLAN_GETCHATUSERNAME_UNFILTERED = 3632;

    public static final int IGNORE_GETNAME_UNFILTERED = 3633;

    public static final int FRIEND_IS_REFERRED = 3634;

    public static final int ACTIVELCANSETTINGS_FIND_LISTENED = 3700;

    public static final int ACTIVECLANSETTINGS_FIND_AFFINED = 3701;

    public static final int ACTIVECLANSETTINGS_GETCLANNAME = 3702;

    public static final int ACTIVECLANSETTINGS_GETALLOWUNAFFINED = 3703;

    public static final int ACTIVECLANSETTINGS_GETRANKTALK = 3704;

    public static final int ACTIVECLANSETTINGS_GETRANKKICK = 3705;

    public static final int ACTIVECLANSETTINGS_GETRANKLOOTSHARE = 3706;

    public static final int ACTIVECLANSETTINGS_GETCOINSHARE = 3707;

    public static final int ACTIVECLANSETTINGS_GETAFFINEDCOUNT = 3709;

    public static final int ACTIVECLANSETTINGS_GETAFFINEDDISPLAYNAME = 3710;

    public static final int ACTIVECLANSETTINGS_GETAFFINEDRANK = 3711;

    public static final int ACTIVECLANSETTINGS_GETBANNEDCOUNT = 3712;

    public static final int ACTIVECLANSETTINGS_GETBANNEDDISPLAYNAME = 3713;

    public static final int ACTIVECLANSETTINGS_GETAFFINEDEXTRAINFO = 3714;

    public static final int ACTIVECLANSETTINGS_GETCURRENTOWNER_SLOT = 3715;

    public static final int ACTIVECLANSETTINGS_GETREPLACEMENTOWNER_SLOT = 3716;

    public static final int ACTIVECLANSETTINGS_GETAFFINEDSLOT = 3717;

    public static final int ACTIVECLANSETTINGS_GETSORTEDAFFINEDSLOT = 3718;

    public static final int AFFINEDCLANSETTINGS_ADDBANNED_FROMCHANNEL = 3719;

    public static final int ACTIVECLANSETTINGS_GETAFFINEDJOINRUNEDAY = 3720;

    public static final int ACTIVECLANCHANNEL_FIND_LISTENED = 3750;

    public static final int ACTIVECLANCHANNEL_FIND_AFFINED = 3751;

    public static final int ACTIVECLANCHANNEL_GETCLANNAME = 3752;

    public static final int ACTIVECLANCHANNEL_GETRANKKICK = 3753;

    public static final int ACTIVECLANCHANNEL_GETRANKTALK = 3754;

    public static final int ACTIVECLANCHANNEL_GETUSERCOUNT = 3755;

    public static final int ACTIVECLANCHANNEL_GETUSERDISPLAYNAME = 3756;

    public static final int ACTIVECLANCHANNEL_GETUSERRANK = 3757;

    public static final int ACTIVECLANCHANNEL_GETUSERWORLD = 3758;

    public static final int ACTIVECLANCHANNEL_KICKUSER = 3759;

    public static final int ACTIVECLANCHANNEL_GETUSERSLOT = 3760;

    public static final int ACTIVECLANCHANNEL_GETSORTEDUSERSLOT = 3761;

    public static final int CLANPROFILE_FIND = 3790;

    public static final int STOCKMARKET_GETOFFERTYPE = 3903;

    public static final int STOCKMARKET_GETOFFERITEM = 3904;

    public static final int STOCKMARKET_GETOFFERPRICE = 3905;

    public static final int STOCKMARKET_GETOFFERCOUNT = 3906;

    public static final int STOCKMARKET_GETOFFERCOMPLETEDCOUNT = 3907;

    public static final int STOCKMARKET_GETOFFERCOMPLETEDGOLD = 3908;

    public static final int STOCKMARKET_ISOFFEREMPTY = 3910;

    public static final int STOCKMARKET_ISOFFERSTABLE = 3911;

    public static final int STOCKMARKET_ISOFFERFINISHED = 3912;

    public static final int STOCKMARKET_ISOFFERADDING = 3913;

    public static final int ADD = 4000;

    public static final int SUB = 4001;

    public static final int MULTIPLY = 4002;

    public static final int DIVIDE = 4003;

    public static final int RANDOM = 4004;

    public static final int RANDOMINC = 4005;

    public static final int INTERPOLATE = 4006;

    public static final int ADDPERCENT = 4007;

    public static final int SETBIT = 4008;

    public static final int CLEARBIT = 4009;

    public static final int TESTBIT = 4010;

    public static final int MODULO = 4011;

    public static final int POW = 4012;

    public static final int INVPOW = 4013;

    public static final int AND = 4014;

    public static final int OR = 4015;

    public static final int MIN = 4016;

    public static final int MAX = 4017;

    public static final int SCALE = 4018;

    public static final int RANDOM_SOUND_PITCH = 4019;

    public static final int HSVTORGB = 4020;

    public static final int APPEND_NUM = 4100;

    public static final int APPEND = 4101;

    public static final int APPEND_SIGNNUM = 4102;

    public static final int LOWERCASE = 4103;

    public static final int FROMDATE = 4104;

    public static final int TEXT_GENDER = 4105;

    public static final int TOSTRING = 4106;

    public static final int COMPARE = 4107;

    public static final int PARAHEIGHT = 4108;

    public static final int PARAWIDTH = 4109;

    public static final int TEXT_SWITCH = 4110;

    public static final int ESCAPE = 4111;

    public static final int APPEND_CHAR = 4112;

    public static final int CHAR_ISPRINTABLE = 4113;

    public static final int CHAR_ISALPHANUMERIC = 4114;

    public static final int CHAR_ISALPHA = 4115;

    public static final int CHAR_ISNUMERIC = 4116;

    public static final int STRING_LENGTH = 4117;

    public static final int SUBSTRING = 4118;

    public static final int REMOVETAGS = 4119;

    public static final int STRING_INDEXOF_CHAR = 4120;

    public static final int STRING_INDEXOF_STRING = 4121;

    public static final int CHAR_TOLOWERCASE = 4122;

    public static final int CHAR_TOUPPERCASE = 4123;

    public static final int TOSTRING_LOCALISED = 4124;

    public static final int STRINGWIDTH = 4125;

    public static final int FORMAT_DATETIME_FROM_MINUTES = 4126;

    public static final int CLANFORUMQFC_TOSTRING = 4127;

    public static final int OC_NAME = 4200;

    public static final int OC_OP = 4201;

    public static final int OC_IOP = 4202;

    public static final int OC_COST = 4203;

    public static final int OC_STACKABLE = 4204;

    public static final int OC_CERT = 4205;

    public static final int OC_UNCERT = 4206;

    public static final int OC_MEMBERS = 4207;

    public static final int OC_PARAM = 4208;

    public static final int OC_ICURSOR = 4209;

    public static final int OC_FIND = 4210;

    public static final int OC_FINDNEXT = 4211;

    public static final int OC_FINDRESTART = 4212;

    public static final int OC_MULTISTACKSIZE = 4213;

    public static final int NC_PARAM = 4300;

    public static final int LC_PARAM = 4400;

    public static final int STRUCT_PARAM = 4500;

    public static final int BAS_GETANIM_READY = 4600;

    public static final int CHAT_GETFILTER_PUBLIC = 5000;

    public static final int CHAT_SETFILTER = 5001;

    public static final int CHAT_SENDABUSEREPORT = 5002;

    public static final int CHAT_GETHISTORY_BYTYPEANDLINE = 5003;

    public static final int CHAT_GETHISTORY_BYUID = 5004;

    public static final int CHAT_GETFILTER_PRIVATE = 5005;

    public static final int CHAT_SETMODE = 5006;

    public static final int CHAT_SENDPUBLIC = 5008;

    public static final int CHAT_SENDPRIVATE = 5009;

    public static final int CHAT_GETHISTORYNAME = 5010;

    public static final int CHAT_GETHISTORYCLAN = 5011;

    public static final int CHAT_GETHISTORYPHRASE = 5012;

    public static final int CHAT_PLAYERNAME = 5015;

    public static final int CHAT_GETFILTER_TRADE = 5016;

    public static final int CHAT_GETHISTORYLENGTH = 5017;

    public static final int CHAT_GETHISTORYNAME_UNFILTERED = 5019;

    public static final int CHAT_PLAYERNAME_UNFILTERED = 5020;

    public static final int CHAT_GETNEXTUID = 5023;

    public static final int CHAT_GETPREVUID = 5024;

    public static final int CHAT_GETHISTORYDISPLAYNAME = 5025;

    public static final int CHATCAT_GETDESC = 5050;

    public static final int CHATCAT_GETSUBCATCOUNT = 5051;

    public static final int CHATCAT_GETSUBCAT = 5052;

    public static final int CHATCAT_GETPHRASECOUNT = 5053;

    public static final int CHATCAT_GETPHRASE = 5054;

    public static final int CHATPHRASE_GETTEXT = 5055;

    public static final int CHATPHRASE_GETAUTORESPONSECOUNT = 5056;

    public static final int CHATPHRASE_GETAUTORESPONSE = 5057;

    public static final int ACTIVECHATPHRASE_PREPARE = 5058;

    public static final int ACTIVECHATPHRASE_SENDPUBLIC = 5059;

    public static final int ACTIVECHATPHRASE_SENDPRIVATE = 5060;

    public static final int ACTIVECHATPHRASE_SENDCLAN = 5061;

    public static final int CHATCAT_GETSUBCATSHORTCUT = 5062;

    public static final int CHATCAT_GETPHRASESHORTCUT = 5063;

    public static final int CHATCAT_FINDSUBCATBYSHORTCUT = 5064;

    public static final int CHATCAT_FINDPHRASEBYSHORTCUT = 5065;

    public static final int CHATPHRASE_GETDYNAMICCOMMANDCOUNT = 5066;

    public static final int CHATPHRASE_GETDYNAMICCOMMAND = 5067;

    public static final int ACTIVECHATPHRASE_SETDYNAMICINT = 5068;

    public static final int ACTIVECHATPHRASE_SETDYNAMICOBJ = 5069;

    public static final int CHATPHRASE_GETDYNAMICCOMMANDPARAM_ENUM = 5070;

    public static final int CHATPHRASE_FIND = 5071;

    public static final int CHATPHRASE_FINDNEXT = 5072;

    public static final int CHATPHRASE_FINDRESTART = 5073;

    public static final int KEYHELD_ALT = 5100;

    public static final int KEYHELD_CTRL = 5101;

    public static final int KEYHELD_SHIFT = 5102;

    public static final int WORLDMAP_SETZOOM = 5200;

    public static final int WORLDMAP_GETZOOM = 5201;

    public static final int WORLDMAP_SETMAP = 5205;

    public static final int WORLDMAP_GETMAP = 5206;

    public static final int ORLDMAP_GETMAPNAME = 5207;

    public static final int WORLDMAP_GETSIZE = 5208;

    public static final int WORLDMAP_GETDISPLAYPOSITION = 5209;

    public static final int WORLDMAP_GETCONFIGORIGIN = 5210;

    public static final int WORLDMAP_GETCONFIGBOUNDS = 5211;

    public static final int WORLDMAP_LISTELEMENT_START = 5212;

    public static final int WORLDMAP_LISTELEMENT_NEXT = 5213;

    public static final int WORLDMAP_JUMPTOSOURCECOORD = 5214;

    public static final int WORLDMAP_COORDINMAP = 5215;

    public static final int WORLDMAP_GETCONFIGZOOM = 5218;

    public static final int WORLDMAP_ISLOADED = 5220;

    public static final int WORLDMAP_JUMPTODISPLAYCOORD = 5221;

    public static final int WORLDMAP_GETSOURCEPOSITION = 5222;

    public static final int WORLDMAP_SETMAP_COORD = 5223;

    public static final int WORLDMAP_GETDISPLAYCOORD = 5224;

    public static final int WORLDMAP_GETSOURCECOORD = 5225;

    public static final int WORLDMAP_FLASHELEMENT = 5226;

    public static final int WORLDMAP_SETMAP_COORD_OVERRIDE = 5227;

    public static final int WORLDMAP_DISABLEELEMENTS = 5228;

    public static final int WORLDMAP_GETDISABLEELEMENTS = 5229;

    public static final int WORLDMAP_FLASHELEMENTCATEGORY = 5230;

    public static final int WORLDMAP_DISABLEELEMENTCATEGORY = 5231;

    public static final int WORLDMAP_GETDISABLEELEMENTCATEGORY = 5232;

    public static final int WORLDMAP_DISABLEELEMENT = 5233;

    public static final int WORLDMAP_GETDISABLEELEMENT = 5234;

    public static final int WORLDMAP_GETCURRENTMAP = 5235;

    public static final int WORLDMAP_FINDNEARESTELEMENT = 5236;

    public static final int WORLDMAP_CLOSEMAP = 5237;

    public static final int FULLSCREEN_ENTER = 5300;

    public static final int FULLSCREEN_EXIT = 5301;

    public static final int FULLSCREEN_MODECOUNT = 5302;

    public static final int FULLSCREEN_GETMODE = 5303;

    public static final int FULLSCREEN_LASTMODE = 5305;

    public static final int GETWINDOWMODE = 5306;

    public static final int SETWINDOWMODE = 5307;

    public static final int GETDEFAULTWINDOWMODE = 5308;

    public static final int SETDEFAULTWINDOWMODE = 5309;

    public static final int OPENURL = 5400;

    public static final int SETCLIENTPALETTE = 5401;

    public static final int SPLINE_NEW = 5405;

    public static final int SPLINE_ADDPOINT = 5406;

    public static final int SPLINE_LENGTH = 5407;

    public static final int QUIT = 5411;

    public static final int LASTLOGIN = 5419;

    public static final int OPENURL_NOLOGIN = 5421;

    public static final int WRITECONSOLE = 5423;

    public static final int FORMATMINIMENU = 5424;

    public static final int DEFAULTMINIMENU = 5425;

    public static final int SETDEFAULTCURSORS = 5426;

    public static final int SETHARDCODEDOPCURSORS = 5427;

    public static final int MINIMENUOPEN = 5428;

    public static final int DOCHEAT = 5429;

    public static final int NOTIFY_ACCOUNTCREATED = 5430;

    public static final int NOTIFY_ACCOUNTCREATESTARTED = 5431;

    public static final int GETCLIPBOARD = 5432;

    public static final int SETSUBMENUMINLENGTH = 5433;

    public static final int CAN_RUN_JAVA_CLIENT = 5436;

    public static final int CAM_MOVETO = 5500;

    public static final int CAM_LOOKAT = 5501;

    public static final int CAM_MOVEALONG = 5502;

    public static final int CAM_RESET = 5503;

    public static final int CAM_FORCEANGLE = 5504;

    public static final int CAM_GETANGLE_XA = 5505;

    public static final int CAM_GETANGLE_YA = 5506;

    public static final int CAM_INC_Y = 5507;

    public static final int CAM_DEC_Y = 5508;

    public static final int CAM_INC_X = 5509;

    public static final int CAM_DEC_X = 5510;

    public static final int CAM_SMOOTH_RESET = 5512;

    public static final int CAM_FOLLOWCOORD = 5511;

    public static final int CAM_REMOVEROOF = 5517;

    public static final int LOGIN_REQUEST = 5600;

    public static final int LOGIN_CONTINUE = 5601;

    public static final int LOGIN_RESETREPLY = 5602;

    public static final int CREATE_AVAILABLEREQUEST = 5604;

    public static final int CREATE_CONNECTREQUEST = 5605;

    public static final int LOGIN_REPLY = 5607;

    public static final int LOGIN_HOPTIME = 5608;

    public static final int CREATE_REPLY = 5609;

    public static final int LOGIN_DISALLOWRESULT = 5611;

    public static final int LOBBY_ENTERGAME = 5612;

    public static final int LOBBY_ENTERGAME_REPLY = 5613;

    public static final int LOBBY_ENTERLOBBY = 5615;

    public static final int LOBBY_LEAVELOBBY = 5616;

    public static final int LOBBY_ENTERLOBBYREPLY = 5617;

    public static final int USERFLOW_FLAGS = 5624;

    public static final int CREATE_UNDER13 = 5625;

    public static final int CREATE_SETUNDER13 = 5626;

    public static final int LOGIN_LAST_TRANSFER_REPLY = 5627;

    public static final int LOGIN_INPROGRESS = 5628;

    public static final int LOGIN_QUEUE_POSITION = 5629;

    public static final int LOGIN_CANCEL = 5630;

    public static final int LOGIN_REQUEST_SOCIAL_NETWORK = 5631;

    public static final int LOBBY_ENTERLOBBY_SOCIAL_NETWORK = 5632;

    public static final int LOGIN_DISALLOWETRIGGER = 5633;

    public static final int DETAIL_BRIGHTNESS = 6001;

    public static final int DETAIL_REMOVEROOFS_OPTION = 6003;

    public static final int DETAIL_GROUNDDECOR_ON = 6005;

    public static final int DETAIL_IDLEANIMS_MANY = 6007;

    public static final int DETAIL_FLICKERING_ON = 6008;

    public static final int DETAIL_SPOTSHADOWS_ON = 6010;

    public static final int DETAIL_HARDSHADOWS = 6011;

    public static final int DETAIL_LIGHTDETAIL_HIGH = 6012;

    public static final int DETAIL_WATERDETAIL_HIGH = 6014;

    public static final int DETAIL_FOG_ON = 6015;

    public static final int DETAIL_ANTIALIASING_QUALITY = 6016;

    public static final int DETAIL_STEREO = 6017;

    public static final int DETAIL_SOUNDVOL = 6018;

    public static final int DETAIL_MUSICVOL = 6019;

    public static final int DETAIL_BGSOUNDVOL = 6020;

    public static final int DETAIL_REMOVEROOFS_OPTION_OVERRIDE = 6021;

    public static final int DETAIL_PARTICLES = 6023;

    public static final int DETAIL_ANTIALIASING_DEFAULT = 6024;

    public static final int DETAIL_BUILDAREA = 6025;

    public static final int DETAIL_BLOOM = 6027;

    public static final int DETAIL_CUSTOMCURSORS = 6028;

    public static final int DETAIL_IDLEANIMS = 6029;

    public static final int DETAIL_GROUNDBLOUNDING = 6030;

    public static final int DETAIL_TOOLKIT = 6031;

    public static final int DETAIL_TOOLKIT_DEFAULT = 6032;

    public static final int DETAIL_CPUUSAGE = 6033;

    public static final int DETAIL_TEXTURING = 6034;

    public static final int DETAIL_ANIMDETAIL = 6035;

    public static final int DETAIL_MAXSCREENSIZE = 6036;

    public static final int DETAIL_SPEECHVOL = 6037;

    public static final int DETAIL_LOGINVOL = 6038;

    public static final int DETAIL_LOADINGSCREENTYPE = 6039;

    public static final int DETAIL_ORTHOGRAPHIC = 6040;

    public static final int DETAIL_SKYDETAIL = 6041;

    public static final int DETAILGET_BRIGHTNESS = 6101;

    public static final int DETAILGET_ANIMDETAIL = 6102;

    public static final int DETAILGET_REMOVEROOFS_OPTIONS = 6103;

    public static final int DETAILGET_GROUNDDECOR_ON = 6105;

    public static final int DETAILGET_IDLEANIMS_MANY = 6107;

    public static final int DETAILGET_FLICKERING_ON = 6108;

    public static final int DETAILGET_SPOTSHADOWS = 6110;

    public static final int DETAILGET_HARDSHADOWS = 6111;

    public static final int DETAILGET_LIGHTDETAIL_HIGH = 6112;

    public static final int DETAILGET_WATERDETAIL_HIGH = 6114;

    public static final int DETAILGET_FOG_ON = 6115;

    public static final int DETAILGET_ANTIALIASING_QUALITY = 6116;

    public static final int DETAILGET_STEREO = 6117;

    public static final int DETAILGET_SOUNDVOL = 6118;

    public static final int DETAILGET_MUSICVOL = 6119;

    public static final int DETAILGET_BGSOUNDVOL = 6120;

    public static final int DETAILGET_PARTICLES = 6123;

    public static final int DETAILGET_ANTIALIASING = 6124;

    public static final int DETAILGET_BUILDAREA = 6125;

    public static final int DETAILGET_BLOOM = 6127;

    public static final int DETAILGET_CUSTOMCURSORS = 6128;

    public static final int DETAILGET_IDLEANIMS = 6129;

    public static final int DETAILGET_GROUNDBLENDING = 6130;

    public static final int DETAILGET_TOOLKIT = 6131;

    public static final int DETAILGET_TOOLKIT_DEFAULT = 6132;

    public static final int DETAILGET_CPUUSAGE = 6135;

    public static final int DETAILGET_TEXTURING = 6136;

    public static final int DETAILGET_PERFORMANCE_METRIC = 6138;

    public static final int DETAILGET_MAXSCREENSIZE = 6139;

    public static final int DETAILGET_SPEECHVOL = 6142;

    public static final int DETAILGET_LOGINVOL = 6143;

    public static final int DETAILGET_SAFEMODE = 6144;

    public static final int DETAILGET_LOADINGSCREENTYPE = 6145;

    public static final int DETAILGET_ORTHOGRAPHIC = 6146;

    public static final int DETAILGET_CANCHOOSESAFEMODE = 6147;

    public static final int DETAILGET_CHOSESAFEMODE = 6148;

    public static final int DETAILGET_SKYDETAIL = 6149;

    public static final int VIEWPORT_SETFOV = 6200;

    public static final int VIEWPORT_SETZOOM = 6201;

    public static final int VIEWPORT_CLAMPFOV = 6202;

    public static final int VIEWPORT_GETEFFECTIVESIZE = 6203;

    public static final int VIEWPORT_GETZOOM = 6204;

    public static final int VIEWPORT_GETFOV = 6205;

    public static final int DATE_MINUTES = 6300;

    public static final int DATE_RUNEDAY = 6301;

    public static final int DATE_RUNEDAY_FROMDATE = 6302;

    public static final int DATE_YEAR = 6303;

    public static final int DATE_ISLEAPYEAR = 6304;

    public static final int DATE_RUNEDAY_TODATE = 6305;

    public static final int DATE_MINUTES_FROMRUNEDAY = 6306;

    public static final int WORLDLIST_FETCH = 6500;

    public static final int WORLDLIST_START = 6501;

    public static final int WORLDLIST_NEXT = 6502;

    public static final int WORLDLIST_SWITCH = 6503;

    public static final int WORLDLIST_SPECIFIC = 6506;

    public static final int WORLDLIST_SORT = 6507;

    public static final int WORLDLIST_AUTOWORLD = 6508;

    public static final int WORLDLIST_PINGWORLDS = 6509;

    public static final int WORLDLIST_SPECIFIC_THISWORLD = 6510;

    public static final int IF_DEBUG_GETOPENIFCOUNT = 6700;

    public static final int IF_GETTOP = 6701;

    public static final int IF_DEBUG_GETNAME = 6702;

    public static final int IF_DEBUG_GETCOMCOUNT = 6703;

    public static final int IF_DEBUG_GETCOMNAME = 6704;

    public static final int IF_DEBUG_GETSERVERTRIGGERS = 6705;

    public static final int IF_DEBUG_BUTTON1 = 6707;

    public static final int IF_DEBUG_BUTTON2 = 6708;

    public static final int IF_DEBUG_BUTTON3 = 6709;

    public static final int IF_DEBUG_BUTTON4 = 6710;

    public static final int IF_DEBUG_BUTTON5 = 6711;

    public static final int IF_DEBUG_BUTTON6 = 6712;

    public static final int IF_DEBUG_BUTTON7 = 6713;

    public static final int IF_DEBUG_BUTTON8 = 6714;

    public static final int IF_DEBUG_BUTTON9 = 6715;

    public static final int IF_DEBUG_BUTTON10 = 6716;

    public static final int IF_DEBUG_TARGET = 6717;

    public static final int MEC_TEXT = 6800;

    public static final int MEC_SPRITE = 6801;

    public static final int MEC_TEXTSIZE = 6802;

    public static final int MEC_CATEGORY = 6803;

    public static final int MEC_PARAM = 6804;

    public static final int USERDETAIL_QUICKCHAT = 6900;

    public static final int USERDETAIL_LOBBY_MEMBERSHIP = 6901;

    public static final int USERDETAIL_LOBBY_RECOVERYDAY = 6902;

    public static final int USERDETAIL_LOBBY_UNREADMESSAGES = 6903;

    public static final int USERDETAIL_LOBBY_LASTLOGINDAY = 6904;

    public static final int USERDETAIL_LOBBY_LASTLOGINADDRESS = 6905;

    public static final int USERDETAIL_LOBBY_EMAILSTATUS = 6906;

    public static final int USERDETAIL_LOBBY_CCEXPIRY = 6907;

    public static final int USERDETAIL_LOBBY_GRACEEXPIRY = 6908;

    public static final int USERDETAIL_LOBBY_DOBREQUESTED = 6909;

    public static final int UDETAIL_DOB = 6910;

    public static final int USERDETAIL_LOBBY_MEMBERSSTATS = 6911;

    public static final int USERDETAIL_LOBBY_PLAYAGE = 6912;

    public static final int USERDETAIL_LOBBY_JCOINS_BALANCE = 6913;

    public static final int USERDETAIL_LOBBY_LOYALTY = 6914;

    public static final int USERDETAIL_LOBBY_LOYALTYBALANCE = 6915;

    public static final int AUTOSETUP_DOSETUP = 7000;

    public static final int AUTOSETUP_SETHIGH = 7001;

    public static final int AUTOSETUP_SETMEDIUM = 7002;

    public static final int AUTOSETUP_SETLOW = 7003;

    public static final int AUTOSETUP_SETMIN = 7004;

    public static final int AUTOSETUP_SETCUSTOM = 7005;

    public static final int AUTOSETUP_BLACKFLAGLAST = 7006;

    public static final int AUTOSETUP_GETLEVEL = 7007;

    public static final int VIDEO_ADVERT_PLAY = 7100;

    public static final int VIDEO_ADVERT_FORCE_REMOVE = 7101;

    public static final int VIDEO_ADVERT_ALLOW_SKIP = 7102;

    public static final int DETAILCANMOD_GROUNDDECOR = 7201;

    public static final int DETAILCANMOD_SPOTSHADOWS = 7202;

    public static final int DETAILCANMOD_HARDSHADOWS = 7203;

    public static final int DETAILCANMOD_WATERDETAIL = 7204;

    public static final int DETAILCANMOD_ANTIALIASING = 7205;

    public static final int DETAILCANMOD_PARTICLES = 7206;

    public static final int DETAILCANMOD_BUILDAREA = 7207;

    public static final int DETAILCANMOD_BLOOM = 7208;

    public static final int DETAILCANMOD_GROUNDBLENDING = 7209;

    public static final int DETAILCANMOD_TEXTURING = 7210;

    public static final int DETAILCANMOD_MAXSCREENSIZE = 7211;

    public static final int DETAILCANMOD_FOG = 7212;

    public static final int DETAILCANMOD_ORTHOGRAPHIC = 7213;

    public static final int DETAILCANMOD_TOOLKIT_DEFAULT = 7214;

    public static final int DETAILCANMOD_SKYDETAIL = 7215;

    public static final int DETAILCANSET_GROUNDDECOR = 7301;

    public static final int DETAILCANSET_SPOTSHADOWS = 7302;

    public static final int DETAILCANSET_HARDSHADOWS = 7303;

    public static final int DETAILCANSET_WATERDETAIL = 7304;

    public static final int DETAILCANSET_ANTIALIASING = 7305;

    public static final int DETAILCANSET_PARTICLES = 7306;

    public static final int DETAILCANSET_BUILDAREA = 7307;

    public static final int DETAILCANSET_BLOOM = 7308;

    public static final int DETAILCANSET_GROUNDBLENDING = 7309;

    public static final int DETAILCANSET_TEXTURING = 7310;

    public static final int DETAILCANSET_MAXSCREENSIZE = 7311;

    public static final int DETAILCANSET_FOG = 7312;

    public static final int DETAILCANSET_ORTHOGRAPHIC = 7313;

    public static final int DETAILCANSET_TOOLKIT_DEFAULT = 7314;

    public static final int DETAILCANSET_SKYDETAIL = 7315;

    private ClientScriptOpCode() {
        /* empty */
    }
}
