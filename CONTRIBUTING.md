### If you have a GitHub account or are willing to create one and eager to learn the basics
  - Fork the repo in the top-right corner. ![how to fork](https://docs.github.com/assets/images/help/repository/fork_button.jpg)
  - Clone your forked repo. [how to clone](https://docs.github.com/en/free-pro-team@latest/github/creating-cloning-and-archiving-repositories/cloning-a-repository)
  - Open your local clone of your forked repo.
  - Put your mod from CustomMaps into the _tqcalculator/resources/databases_ directory.
  - Put your modstrings.txt in a folder called text. (you can take a look at the other mods for reference)
  - Put your skill points rewarded by quests in a txt file inside the _questSkillPoints_ folder. (you can take a look at the other mods for reference and the vanilla points)
  - Delete the assets and source directories as they are not required and would bloat the repo.
  - If you have any kind of uncommon directory structure and are familiar with git please have a look at the .gitignore in the databases directory to avoid bloating the repo.
  - Feel free to put some relevant links in a links.txt inside your mod directory. (I suggest copying one of the existing links.txt from another mod)
    - links consist of two parts separated by an arrow like this ``text->url``:
      - the text that will be visible on the site 
      - the url where clicking on the text will take you
  - If you're real fancy you can slim down the repo by:
    - Building the maven project inside tqcalculator
    - Running the resulting program (located in the target directory) __ONCE__
    - Deleting all database directories in _tqcalculator/target/resources/databases_ __not__ named *modname*-cleaned and remove the -cleaned from the new directories
    - Then copy the relevant directories into the _tqcalculator/resources/databases_ directory
  - Add your images to the _tqcalculator/mods/modname/images_ directory (take a look at the other mods to reproduce the correct structure
  - Commit your changes with a message like ``[database] add -modname-``. [how to commit](https://github.com/git-guides/git-commit)
  - Push your local clone. [pushing a repo](https://docs.github.com/en/free-pro-team@latest/github/using-git/pushing-commits-to-a-remote-repository)
  - Create a [pull request](https://github.com/ByteSquire/TitanQuestCalculator/pulls). (you can take a look at [the example pull request](https://github.com/ByteSquire/TitanQuestCalculator/pull/1))
  - That's it you're done, you can now wait for [someone](https://github.com/ByteSquire) to merge the pull and push the result

### If you do not want to create a Github account
  - Get in touch with me(@Zaphodgame) via the [Titan Forge discord channel](https://discord.gg/efFsGMJ8tn)
  - Create an [issue](https://github.com/ByteSquire/TitanQuestCalculator/issues/new?assignees=ByteSquire&labels=&template=mod-addition.md&title=%5BModAddition%5D)
