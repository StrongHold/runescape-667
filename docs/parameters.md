# Parameters

The `runescape.jar` is configured using parameters, passed either as positional
arguments via the [command line](cli.md) or as `<param>` elements in an
[applet](applet.md).

## worldid

The numerical identifier of the [game world](https://runescape.wiki/w/Server)
the client is connecting to.

## lobbyid

The numerical identifier of the [game lobby](https://runescape.wiki/w/RuneScape_Lobby)
the client is connecting to.

## [modewhere](../runescape/src/main/java/com/jagex/core/constants/ModeWhere.java)

The environment the client is connecting to.

Running via the command-line **always** sets `modewhere` to `local`.

Running via an applet **always** sets `modewhere` to `wip`.

Any `modewhere` other than `live` will display the client's debug panel and
enable debug commands for administrators.

|   | ModeWhere | Description       |
|--:|:----------|:------------------|
| 0 | LIVE      | Live              |
| 1 | RC        | Release candidate |
| 2 | QA        | Quality assurance |
| 3 | WIP       | Work in progress  |
| 4 | LOCAL     | Local             |
| 5 | WTI       | Internal          |
| 6 | INTBETA   | Internal beta     |

### Ports

Each `modewhere` specifies a different set of ports the client will use to connect. The
first port listed is the primary, dialled first; the client rotates to the secondary
after a failed dial (see `ConnectionInfo.rotateMethods`):

| ModeWhere                                  |                      Lobby&nbsp;server                      |                      Game&nbsp;server                       |      Web&nbsp;server       |
|:-------------------------------------------|:-----------------------------------------------------------:|:-----------------------------------------------------------:|:--------------------------:|
| LIVE                                       |                        443,<br>43594                        |                        443,<br>43594                        |             80             |
| RC<br>QA<br>WIP<br>LOCAL<br>WTI<br>INTBETA | 50000&nbsp;+&nbsp;`worldid`,<br>40000&nbsp;+&nbsp;`worldid` | 50000&nbsp;+&nbsp;`worldid`,<br>40000&nbsp;+&nbsp;`worldid` | 7000&nbsp;+&nbsp;`worldid` |

### Domains

Each `modewhere` specifies a different prefix to the website domain:

| ModeWhere | Prefix    |
|:----------|:----------|
| `LIVE`    | www       |
| `WTRC`    | www-wtrc  |
| `WTQA`    | www-wtqa  |
| `WTWIP`   | www-wtwip |
| `LOCAL`   | www       |
| `WTI`     | www       |
| `INTBETA` | www       |

## [modewhat](../runescape/src/main/java/com/jagex/core/constants/ModeWhat.java)

The mode of the client.

A `modewhat` of `wip` will enable debug operations in client scripts.

The ID of the client's file system is calculated by adding 32 to the ID of the specified `modewhat`.

|   | ModeWhat | Description       |
|--:|:---------|:------------------|
| 0 | `LIVE`   | Live              |
| 1 | `RC`     | Release candidate |
| 2 | `WIP`    | Work in progress  |

## lang

The language of the client.

|   | Language     |
|--:|:-------------|
| 0 | `ENGLISH`    |
| 1 | `GERMAN`     |
| 2 | `FRENCH`     |
| 3 | `PORTUGUESE` |

## objecttag

Set to `1` if the client is loaded an `<object>` element.

## js

Set to `1` if the client is loaded in an environment that supports JavaScript.

## advert

Set to `1` if an advertisement is shown alongside the client.

## [game](../runescape/src/main/java/com/jagex/core/constants/ModeGame.java)

The type of game the client is running.

|   | game           | Description                                                |
|--:|:---------------|:-----------------------------------------------------------|
| 0 | `RUNESCAPE`    | [RuneScape](https://en.wikipedia.org/wiki/RuneScape)       |
| 1 | `STELLAR_DAWN` | [Stellar Dawn](https://en.wikipedia.org/wiki/Stellar_Dawn) |
| 2 | `GAME_3`       |                                                            |
| 3 | `GAME_4`       |                                                            |

## affid

The player's affiliate program numerical identifier.

## quiturl

The URL to open in the player's browser when quitting the game.

## settings

An encoded string of the client's settings.

## under

Set to `1` if the player is under the age of 13.

## country

The country the player is playing from.

|     | Country&nbsp;code | Country&nbsp;name                            |
|----:|:-----------------:|:---------------------------------------------|
|   0 |     RUNESCAPE     | RuneScape                                    |
|   1 |       GE_AB       | Abkhazia                                     |
|   2 |        AF         | Afghanistan                                  |
|   3 |        AX         | Aland                                        |
|   4 |        AL         | Albania                                      |
|   5 |        DZ         | Algeria                                      |
|   6 |        AS         | American Samoa                               |
|   7 |        AD         | Andorra                                      |
|   8 |        AO         | Angola                                       |
|   9 |        AI         | Anguilla                                     |
|  10 |        AQ         | Antarctica                                   |
|  11 |        AG         | Antigua and Barbuda                          |
|  12 |        AR         | Argentina                                    |
|  13 |        AM         | Armenia                                      |
|  14 |        AW         | Aruba                                        |
|  15 |        AU         | Australia                                    |
|  16 |        AT         | Austria                                      |
|  17 |        AZ         | Azerbaijan                                   |
|  18 |        BS         | Bahamas                                      |
|  19 |        BH         | Bahrain                                      |
|  20 |        BD         | Bangladesh                                   |
|  21 |        BB         | Barbados                                     |
|  22 |        BY         | Belarus                                      |
|  23 |        BE         | Belgium                                      |
|  24 |        BZ         | Belize                                       |
|  25 |        BJ         | Benin                                        |
|  26 |        BM         | Bermuda                                      |
|  27 |        BT         | Bhutan                                       |
|  28 |        BO         | Bolivia                                      |
|  29 |        BA         | Bosnia and Herzegovina                       |
|  30 |        BW         | Botswana                                     |
|  31 |        BR         | Brazil                                       |
|  32 |        VG         | British Virgin Islands                       |
|  33 |        BN         | Brunei                                       |
|  34 |        BG         | Bulgaria                                     |
|  35 |        BF         | Burkina Faso                                 |
|  36 |        BI         | Burundi                                      |
|  37 |        KH         | Cambodia                                     |
|  38 |        CM         | Cameroon                                     |
|  39 |        CA         | Canada                                       |
|  40 |        CV         | Cape Verde                                   |
|  41 |        KY         | Cayman Islands                               |
|  42 |        CF         | Central African Republic                     |
|  43 |        TD         | Chad                                         |
|  44 |        CL         | Chile                                        |
|  45 |        CN         | China                                        |
|  46 |        CO         | Columbia                                     |
|  47 |   COMMONWEALTH    | Commonwealth                                 |
|  48 |        KM         | Comoros                                      |
|  49 |        CR         | Costa Rica                                   |
|  50 |        CI         | Cote d'Ivoire                                |
|  51 |        HR         | Croatia                                      |
|  52 |        CU         | Cuba                                         |
|  53 |        CY         | Cyprus                                       |
|  54 |        CZ         | Czech Republic                               |
|  55 |        CD         | Democratic Republic of the Congo             |
|  56 |        DK         | Denmark                                      |
|  57 |        DJ         | Djibouti                                     |
|  58 |        DM         | Dominica                                     |
|  59 |        DO         | Dominican Republic                           |
|  60 |        TL         | East Timor                                   |
|  61 |        EC         | Ecuador                                      |
|  62 |        EG         | Egypt                                        |
|  63 |        SV         | El Salvador                                  |
|  64 |      GB_ENG       | England                                      |
|  65 |        GQ         | Equatorial Guinea                            |
|  66 |        ER         | Eritrea                                      |
|  67 |        EE         | Estonia                                      |
|  68 |        ET         | Ethiopia                                     |
|  69 |        EU         | European Union                               |
|  70 |        FK         | Falkland Islands                             |
|  71 |        FO         | Faroes                                       |
|  72 |        FJ         | Fiji                                         |
|  73 |        FI         | Finland                                      |
|  74 |        FR         | France                                       |
|  75 |        GA         | Gabon                                        |
|  76 |        GM         | Gambia                                       |
|  77 |        GE         | Georgia                                      |
|  78 |        DE         | Germany                                      |
|  79 |        GH         | Ghana                                        |
|  80 |        GR         | Greece                                       |
|  81 |        GL         | Greenland                                    |
|  82 |        GD         | Grenada                                      |
|  83 |        GU         | Guam                                         |
|  84 |        GT         | Guatemala                                    |
|  85 |        GG         | Guernsey                                     |
|  86 |        GN         | Guinea                                       |
|  87 |        GW         | Guinea Bissau                                |
|  88 |        GY         | Guyana                                       |
|  89 |        HT         | Haiti                                        |
|  90 |        HN         | Honduras                                     |
|  91 |        HK         | Hong Kong                                    |
|  92 |        HU         | Hungary                                      |
|  93 |        IS         | Iceland                                      |
|  94 |        IN         | India                                        |
|  95 |        ID         | Indonesia                                    |
|  96 |        IR         | Iran                                         |
|  97 |        IQ         | Iraq                                         |
|  98 |        IE         | Ireland                                      |
|  99 |        IM         | Isle of Man                                  |
| 100 |        IL         | Israel                                       |
| 101 |        JM         | Jamaica                                      |
| 102 |        IT         | Italy                                        |
| 103 |        JP         | Japan                                        |
| 104 |        JE         | Jersey                                       |
| 105 |        JO         | Jordan                                       |
| 106 |        KZ         | Kazakstan                                    |
| 107 |        KE         | Kenya                                        |
| 108 |        KI         | Kiribati                                     |
| 109 |        XK         | Kosovo                                       |
| 110 |        KW         | Kuwait                                       |
| 111 |        KG         | Kyrgyzstan                                   |
| 112 |        LA         | Laos                                         |
| 113 |        LV         | Latvia                                       |
| 114 |        LB         | Lebanon                                      |
| 115 |        LS         | Lesotho                                      |
| 116 |        LR         | Liberia                                      |
| 117 |        LY         | Libya                                        |
| 118 |        LI         | Liechtenstein                                |
| 119 |        LT         | Lithuania                                    |
| 120 |        LU         | Luxembourg                                   |
| 121 |        MO         | Macau                                        |
| 122 |        MK         | Macedonia                                    |
| 123 |        MG         | Madagascar                                   |
| 124 |        MW         | Malawi                                       |
| 125 |        MY         | Malaysia                                     |
| 126 |        MV         | Maldives                                     |
| 127 |        ML         | Mali                                         |
| 128 |        MT         | Malta                                        |
| 129 |       MARS        | Mars                                         |
| 130 |        MH         | Marshall Islands                             |
| 131 |        MR         | Mauritania                                   |
| 132 |        MU         | Mauritius                                    |
| 133 |        MX         | Mexico                                       |
| 134 |        FM         | Micronesia                                   |
| 135 |        MD         | Moldova                                      |
| 136 |        MC         | Monaco                                       |
| 137 |        MN         | Mongolia                                     |
| 138 |        ME         | Montenegro                                   |
| 139 |        MS         | Montserrat                                   |
| 140 |        MA         | Morocco                                      |
| 141 |        MZ         | Mozambique                                   |
| 142 |        MM         | Myanmar                                      |
| 143 |        AZE        | Nagorno Karabakh                             |
| 144 |        NA         | Namibia                                      |
| 145 |       NATO        | NATO                                         |
| 146 |        NR         | Nauru                                        |
| 147 |        NP         | Nepal                                        |
| 148 |        NL         | Netherlands                                  |
| 149 |        AN         | Netherlands Antilles                         |
| 150 |        NZ         | New Zealand                                  |
| 151 |        NI         | Nicaragua                                    |
| 152 |        NE         | Niger                                        |
| 153 |        NG         | Nigeria                                      |
| 154 |        NF         | Norfolk Island                               |
| 155 |     NORTH_CY      | North Cyprus                                 |
| 156 |        MP         | Northern Mariana Islands                     |
| 157 |        KP         | North Korea                                  |
| 158 |        NO         | Norway                                       |
| 159 |     OLYMPICS      | Olympics                                     |
| 160 |        OM         | Oman                                         |
| 161 |        PK         | Pakistan                                     |
| 162 |        PW         | Palau                                        |
| 163 |        PS         | Palestine                                    |
| 164 |        PA         | Panama                                       |
| 165 |        PG         | Papua New Guinea                             |
| 166 |        PY         | Paraguay                                     |
| 167 |        PE         | Peru                                         |
| 168 |        PH         | Philippines                                  |
| 169 |        PN         | Pitcairn Islands                             |
| 170 |        PL         | Poland                                       |
| 171 |        PT         | Portugal                                     |
| 172 |        PR         | Puerto Rico                                  |
| 173 |        QA         | Qatar                                        |
| 174 |     RED_CROSS     | Red Cross                                    |
| 175 |        CG         | Republic of the Congo                        |
| 176 |        RO         | Romania                                      |
| 177 |        RU         | Russia                                       |
| 178 |        RW         | Rwanda                                       |
| 179 |        BL         | Saint Barthelemy                             |
| 180 |        SH         | Saint Helena                                 |
| 181 |        KN         | Saint Kitts and Nevis                        |
| 182 |        LC         | Saint Lucia                                  |
| 183 |        VC         | Saint Vincent and the Grenadines             |
| 184 |        WS         | Samoa                                        |
| 185 |        SM         | San Marino                                   |
| 186 |        ST         | Sao Tome and Principe                        |
| 187 |        SA         | Saudi Arabia                                 |
| 188 |      GB_SCT       | Scotland                                     |
| 189 |        SN         | Senegal                                      |
| 190 |        RS         | Serbia                                       |
| 191 |        SC         | Seychelles                                   |
| 192 |        SL         | Sierra Leone                                 |
| 193 |        SG         | Singapore                                    |
| 194 |        SK         | Slovakia                                     |
| 195 |        SI         | Slovenia                                     |
| 196 |        SB         | Solomon Islands                              |
| 197 |        SO         | Somalia                                      |
| 198 |    SOMALILAND     | Somaliland                                   |
| 199 |        ZA         | South Africa                                 |
| 200 |        GS         | South Georgia and the South Sandwich Islands |
| 201 |        KR         | South Korea                                  |
| 202 |   SOUTH_OSSETIA   | South Ossetia                                |
| 203 |        ES         | Spain                                        |
| 204 |        LK         | Sri Lanka                                    |
| 205 |        SD         | Sudan                                        |
| 206 |        SR         | Suriname                                     |
| 207 |        SZ         | Swaziland                                    |
| 208 |        SE         | Sweden                                       |
| 209 |        CH         | Switzerland                                  |
| 210 |        SY         | Syria                                        |
| 211 |        TW         | Taiwan                                       |
| 212 |        TJ         | Tajikistan                                   |
| 213 |        TZ         | Tanzania                                     |
| 214 |        TH         | Thailand                                     |
| 215 |        TG         | Togo                                         |
| 216 |        TO         | Tonga                                        |
| 217 |        TT         | Trinidad and Tobago                          |
| 217 |        TN         | Tunisia                                      |
| 219 |        TR         | Turkey                                       |
| 220 |        TM         | Turkmenistan                                 |
| 221 |        TC         | Turks and Caicos Islands                     |
| 222 |        TV         | Tuvalu                                       |
| 223 |        UG         | Uganda                                       |
| 224 |        UA         | Ukraine                                      |
| 225 |        AE         | United Arab Emirates                         |
| 226 |        GB         | United Kingdom                               |
| 227 |        UN         | United Nations                               |
| 228 |        US         | United States                                |
| 229 |        UY         | Uruguay                                      |
| 230 |        VI         | US Virgin Islands                            |
| 231 |        UZ         | Uzbekistan                                   |
| 233 |        VA         | Vatican City                                 |
| 234 |        VE         | Venezuela                                    |
| 235 |        VN         | Vietnam                                      |
| 236 |      GB_WLS       | Wales                                        |
| 237 |        EH         | Western Sahara                               |
| 238 |       WORLD       | World                                        |
| 239 |        YE         | Yemen                                        |
| 240 |        ZM         | Zambia                                       |
| 241 |        ZW         | Zimbabwe                                     |

## colourid

The colour palette the client uses to draw the loading bar:

|   | Game         | Fill&nbsp;colour | Outline&nbsp;colour | Text&nbsp;colour |
|--:|:-------------|:----------------:|:-------------------:|:----------------:|
| 0 | RuneScape    |     #8C1111      |       #8C1111       |     #FFFFFF      |
| 1 | Stellar Dawn |     #323232      |       #FFFFFF       |     #FFFFFF      |
| 2 |              |     #323232      |       #FF3905       |     #FF7405      |
| 3 |              |     #323232      |       #FF3905       |     #FF7405      |

## sitesettings_member

Set to `1` if the player is a [member](https://runescape.wiki/w/Members).

## frombilling

Set to `true` if the player was redirected to play after purchasing membership

## sskey

A [base64](https://en.wikipedia.org/wiki/Base64) encoded string of the player's
[single sign-on](https://en.wikipedia.org/wiki/Single_sign-on) key.

## force64mb

Set to `true` to force the client to run in under 64mb of RAM

## worldflags

Characteristics of the game world:

|     | Flag       | Description                                                                                  |
|----:|:-----------|:---------------------------------------------------------------------------------------------|
| 0x1 | MEMBERS    | This world is for [members](https://runescape.wiki/w/Members) only.                          |
| 0x2 | QUICK_CHAT | This world permits communication only via [quick-chat](https://runescape.wiki/w/Quick_Chat). |
| 0x4 | PVP        | This world has [player-versus-player](https://runescape.wiki/w/PvP_world) enabled.           |
| 0x8 | LOOT_SHARE | This world has [LootShare](https://runescape.wiki/w/LootShare) enabled.                      |

## userFlow

The value of the user flow cookie.

## additionalInfo

Up to 50 characters of additional client information.

## hc

Set to `1` if the player "has [Chrome](https://blog.google/products/chrome/)".
