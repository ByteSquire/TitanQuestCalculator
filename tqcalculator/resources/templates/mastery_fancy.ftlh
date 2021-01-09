<!DOCTYPE html>
<html onkeydown="shiftPressed(event);">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Titan Quest Calculator - ${name}</title>

    <link rel="stylesheet" type="text/css" href="../../css/styles.css" />

    <script type="text/javascript" src="js/booleans.js"></script>
    <script type="text/javascript" src="js/charAttributes_handler.js"></script>
    <script type="text/javascript" src="js/plusButton_handler.js"></script>
    <script type="text/javascript" src="js/popup_handler.js"></script>
    <script type="text/javascript" src="js/skillButton_handler.js"></script>
    <script type="text/javascript" src="js/JSON_handler.js"></script>
    <script type="text/javascript" src="js/skill.js"></script>
    <script type="text/javascript" src="js/masteryTier_handler.js"></script>
    <script type="text/javascript" src="js/skillPlusButton_handler.js"></script>
    <script type="text/javascript" src="js/attribute_parser.js"></script>
  </head>

  <body oncontextmenu="return false">
    <table class="masteryTable">
      <tr>
        <td class="panels">
          <div style="display: none" id="pop"></div>
          <div class="disabled" id="panel1">
            <img
              class="masteryPanel"
              src="../../MasteryPage/images/masteries/panel/skillpanel.png"
            />
            <button class="plusButton" id="plusButton1" onmousedown="plusClicked(this, event);" onmouseover="plusButtonPopup(this, event);" onmouseout="hidePopup();">
              <img
                class="plusButtonImage"
                src="../../MasteryPage/images/masteries/panel/plusbutton.png"
              />
            </button>
            <button class="allPlusButton" id="allPlusButton1" onmousedown="allPlusClicked(this, event);" onmouseover="allPlusButtonPopup(this, event);" onmouseout="hidePopup();">
              <img
                class="allPlusButtonImage"
                src="../../MasteryPage/images/masteries/panel/plusbutton.png"
              />
              0
            </button>
            <img
              class="masteryImagePanel"
              id="mastery1"
              src="../../MasteryPage/images/masteries/1.png"
            />
            <span class="masteryTier" id="masteryTier1" onmousedown="masteryTierClicked(this.parentElement, 0)">1</span>
            <span class="masteryTier" id="masteryTier2" onmousedown="masteryTierClicked(this.parentElement, 1)">2</span>
            <span class="masteryTier" id="masteryTier3" onmousedown="masteryTierClicked(this.parentElement, 2)">3</span>
            <span class="masteryTier" id="masteryTier4" onmousedown="masteryTierClicked(this.parentElement, 3)">4</span>
            <span class="masteryTier" id="masteryTier5" onmousedown="masteryTierClicked(this.parentElement, 4)">5</span>
            <span class="masteryTier" id="masteryTier6" onmousedown="masteryTierClicked(this.parentElement, 5)">6</span>
            <span class="masteryTier" id="masteryTier7" onmousedown="masteryTierClicked(this.parentElement, 6)">7</span>
            <div id="m0bar" class="bar"></div>
          </div>
          <div class="disabled" id="panel2">
            <img
              class="masteryPanel"
              src="../../MasteryPage/images/masteries/panel/skillpanel.png"
            />
            <button class="plusButton" id="plusButton2" onmousedown="plusClicked(this, event);" onmouseover="plusButtonPopup(this, event);" onmouseout="hidePopup();">
              <img
                class="plusButtonImage"
                src="../../MasteryPage/images/masteries/panel/plusbutton.png"
              />
            </button>
            <button class="allPlusButton" id="allPlusButton2" onmousedown="allPlusClicked(this, event);" onmouseover="allPlusButtonPopup(this, event);" onmouseout="hidePopup();">
              <img
                class="allPlusButtonImage"
                src="../../MasteryPage/images/masteries/panel/plusbutton.png"
              />
              0
            </button>
            <img
              class="masteryImagePanel"
              id="mastery2"
              src="../../MasteryPage/images/masteries/2.png"
            />
            <span class="masteryTier" id="masteryTier1" onmousedown="masteryTierClicked(this.parentElement, 0)">1</span>
            <span class="masteryTier" id="masteryTier2" onmousedown="masteryTierClicked(this.parentElement, 1)">2</span>
            <span class="masteryTier" id="masteryTier3" onmousedown="masteryTierClicked(this.parentElement, 2)">3</span>
            <span class="masteryTier" id="masteryTier4" onmousedown="masteryTierClicked(this.parentElement, 3)">4</span>
            <span class="masteryTier" id="masteryTier5" onmousedown="masteryTierClicked(this.parentElement, 4)">5</span>
            <span class="masteryTier" id="masteryTier6" onmousedown="masteryTierClicked(this.parentElement, 5)">6</span>
            <span class="masteryTier" id="masteryTier7" onmousedown="masteryTierClicked(this.parentElement, 6)">7</span>
            <div id="m0bar" class="bar"></div>
          </div>
        </td>
        <td class="infos">
          <div id="stats">
            <table>
              <tr>
                <td>Level required:</td>
                <td><div id="lvl">1</div></td>
                <td>Maximum level:</td>
                <td><div id="maxLevel">85</div></td>
                <td>Points left:</td>
                <td><div id="pointsLeft">252</div></td>
              </tr>
              <tr>
                <td>Health:</td>
                <td><div id="hp">300+0%</div></td>
                <td>Energy:</td>
                <td><div id="mp">300+0%</div></td>
              </tr>
              <tr>
                <td>Intelligence:</td>
                <td><div id="int">50+0%</div></td>
                <td>Strength:</td>
                <td><div id="str">50+0%</div></td>
                <td>Dexterity:</td>
                <td><div id="dex">50+0%</div></td>
              </tr>
              <tr>
                <td><br></td>
              </tr>
              <tr>
                <td>Quest Skill Points:</td>
                <td>
                    <select id="questPoints" onchange="pointsSelected(this)">
                    <#list questSkillPoints as key, value>
                        <optgroup label="${key}">
                        <#list value as _value>
                            <option value="${_value[0]}">${_value[1]}</option>
                        </#list>
                        </optgroup>
                    </#list>
                    </select>
                </td>
              </tr>
              <tr>
                <td><br></td>
              </tr>
              <tr>
                <td><span style="color: green; font-weight: bold">Controls:</span></td>
              </tr>
              <tr>
                <td>Right Click</td>
                <td>Remove Point</td>
              </tr>
              <tr>
                <td>Left Click</td>
                <td>Add Point</td>
              </tr>
              <tr>
                <td>Shift-Right Click</td>
                <td>Reset Points</td>
              </tr>
              <tr>
                <td>Shift-Left Click</td>
                <td>Add Full Points<td>
              </tr>
              <tr>
                <td>Click Mastery Tier</td>
                <td>Set Points to Tier</td>
              </tr>
              <tr>
                <td>Press Shift over Skill</td>
                <td>Toggle Pet Info</td>
              </tr>
            </table>
          </div>
          <br>
          <div id="skills gained">
            <table>
              <tr>
                <td>Skill1:</td>
                <td><div id="lvl">1</div></td>
              </tr>
              <tr>
                <td>Skill2:</td>
                <td><div id="lvl">1</div></td>
              </tr>
              <tr>
                <td>Skill3:</td>
                <td><div id="lvl">1</div></td>
              </tr>
            </table>
          </div>
        </td>
      </tr>
    </table>
  </body>
</html>
