<div align="center">
  <img src="src/main/resources/internal/icons/program-icon.svg" alt="interactive deck icon" width="256px" />
  <p>Turn your Android phone into a <b><i>Macro Keypad</i></b></p>
  <br><br>
  <div class="code-related-badges">
    <img src="https://custom-icon-badges.demolab.com/github/issues-raw/knighthat/InteractiveDeck-Desktop?logo=issue" alt="open-issue-count">
    <img src="https://custom-icon-badges.demolab.com/github/license/knighthat/InteractiveDeck-Desktop?logo=law" alt="repository-license">
    <img src="https://custom-icon-badges.demolab.com/github/v/tag/knighthat/InteractiveDeck-Desktop?logo=tag&logoColor=white" alt="latest-update-tag">
  </div>
  <br>
  <div class="external-links">
    <a href="https://github.com/knighthat/InteractiveDeck-Desktop/releases">
        <img src="https://custom-icon-badges.demolab.com/badge/-Download-28a745?style=for-the-badge&logo=download&logoColor=white" alt="download-button">
    </a>
    <a href="https://github.com/knighthat/InteractiveDeck-Desktop/issues">
      <img src="https://custom-icon-badges.demolab.com/badge/-Report%20Issue-6f42c1?style=for-the-badge&logoColor=white&logo=issue-opened" alt="open-issue-button">
    </a>
  </div>
</div>

---

# Features

## General

🎯 Easy to use, straight forward.<br>
🔲 Customizable, button's foreground/background/text can be changed.<br>
🚀 Fast to connect, responsive to touch/update between devices.<br>
⚖️ Lightweight, built using minimal resources to maximize performance.
Real-time update, you'll see the result as soon as you press Enter (or Apply)

## Core Functions

📑 Multiple Pages (Profiles)

- Add
- Remove
- Modify
    - Title
    - Columns (up to 10)
    - Rows (up to 10)
    - Gap Between Buttons

🖲️ Buttons

- Label
    - Custom texts, support UTF-8
    - Colorful
    - Supports styling (bold, italic, etc.)
    - Resizable
- Background
- Border
- Task (Its Usage)
    - Executing BASH Script
    - Switch Profile on Mobile Device

:memo: Task (Makes button executable)

- Executing BASH Script
- Switch Profile on Mobile Device

🗔 User Interface

- Friendly & easy-to-use UI
- Font can be changed to any installed font
- Most buttons are showed as icon
- Interactive, responsive to changes

# Installation

## Manual

> These steps require [JDK](https://jdk.java.net/20/) and [MAVEN](https://maven.apache.org/download.cgi) to be installed
> prior.

1. Clone project `git clone --depth 1 https://github.com/knighthat/InteractiveDeck-Desktop`
2. Compile it `mvn package`
3. Start the program (Follow [Usage](#usage))

## Prebuilt

> For bleeding edge features, please follow [manual steps to compile](#manual)

- [ALL RELEASES](https://github.com/knighthat/InteractiveDeck-Desktop/releases)
- [BETA/LATEST](https://github.com/knighthat/InteractiveDeck-Desktop/releases/tag/beta)

<p align="center"><b>THIS SECTION IS SUBJECT TO CHANGE, PLEASE CHECK BACK FOR MORE INFORMATION</b></p>

# Usage

> Before running the program, make sure you've followed [Installation](#installation)

### Requirements

- JRE 17 or higher. [DOWNLOAD HERE](https://jdk.java.net/)

`java -Xms512M -Xmx512M -jar target/InteractiveDeckDesktop-*.jar`

### Additional arguments

- `-Xms512M`: Initial memory to start the app
- `-Xmx512M`: Maximum memory allocation to the app

> My recommendations:
> - Allocate at least 512MB of RAM for best experience.
> - Set both Xms and Xmx to the same number

- `-Dlog.level=INFO`: Set the log level (Default is INFO)

> Available levels:
> - TRACE
> - DEBUG
> - INFO
> - WARN
> - ERROR

<p align="center"><b>THIS SECTION IS SUBJECT TO CHANGE, PLEASE CHECK BACK FOR MORE INFORMATION</b></p>

# Contribute

This project is open to public and anyone can contribute and be a part of it at any given time.
Please follow guidelines to prevent conflict between push

### Guidelines

* Please follow the existing code style and conventions in the projects.
* Be sure to write clear and concise code and documentation.
* If you're adding new features or make significant changes, consider adding relevant tests.
* If you're fixing a bug, please provide steps to reproduce the issue if applicable.

### Code of Conduct

1. **Do**

* **_Be respectful_**: Treat all individuals with respect, regardless of their background, identity, or opinions.
* **_Be inclusive_**: Welcome and include diverse perspectives, experiences, and ideas.
* **_Be understanding_**: Seek to understand differing viewpoints and engage in constructive discussions.
* **_Be mindful_**: Be mindful of the impact our words and actions have on others in the community.

---

2. **Don't**

* **_Harassment_**: Any form of harassment, discrimination, or offensive behavior is unacceptable and will not be
  tolerated.
* **_Trolling_**: Deliberate and disruptive actions or comments with the intent to provoke or offend others.
* **_Hateful speech_**: Use of derogatory or offensive language, slurs, or personal attacks.
* **_Spam_**: Posting irrelevant or unsolicited content, including excessive self-promotion.

# License (MIT)

> TL;DR You are free to obtain, make changes, or even distribute commercially without restrictions.

For details, please read [LICENSE](LICENSE.md)
