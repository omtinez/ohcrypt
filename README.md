
OHCRYPT ENCRYPTION TOOL
=======================

* Developed by omtinez

* Licensed under GPL

* Last edited Oct. 19, 2012

* www.github.com/omtinez/ohcrypt
   
   
   
Release notes for version 0.6.6   


OVERVIEW
--------

OhCrypt lets you easily encrypt and decrypt files using some of the most popular algorithms, like AES, Blowfish and TwoFish. With OhCrypt, you can secure your data with a password-based encryption algorithm through a clean and simple interface. It is cross-platform software and files can be encrypted and decrypted on any machine that can run a Java Virtual Machine (JVM).


CHANGELOG
---------

* IN THIS VERSION
  
  - Fixed several warnings for latest version of Eclipse


* IN 0.6.5

  - blowfish now supported (experimental)

  - cleaned up ohcrypt class

  - deactivated serpent and twofish


* IN 0.6.4

  - fixed nasty bug in portable mode under windows

  - improved stream handling of files


* IN 0.6.3

  - ohcrypt now developed using eclipse IDE

  - reorganized settings class

  - ohcrypt window now organized in tabs, as suggested by sourceforge user thotheolh

  - added option for compressing the encrypted files

  - fixed bug: correct destination folder when using portable mode


* IN 0.6.2

  - portable mode renamed

  - portable mode improved

  - on-the-fly encryption somewhat implemented

  - classes and functions reorganized into different files


* IN 0.6.1

  - implemented safedelete function, testing with 20 rewrites

  - more functions write on the console to help debugging

  - minor tweaks in labels and tooltips


* IN 0.6

  - settings menu expanded

  - some tooltips enabled

  - cleaned up the code, split in two different classes

  - experimental auto-extract of encrypted files feature


* IN 0.5

  - milestone release

  - internal file handling changed

  - real multiple file encryption

  - files packaged before encryption

  - settings menu inluded


* IN 0.4.2

  - multiple file selection implemented

  - fixed labels for filenames

  - implemented JList to display file paths

  - overall improvement in the look&feeling of the program

  - optimization of the code, deleting over 50 lines


* IN 0.4.1

  - implemented header at the beginning of encrypted files


* IN 0.4

  - fixed unexpected behavior when using the program several times within same instance

  - self explanatory title for file choosers

  - encryption performance 500% faster

  - twofish and serpent encryption algorithms added (not working yet)

  - jar package cleaner

  - updated developer info


* IN 0.3.4

  - real multiple file encryption / decryption implemented

  - "Not output file selected" always popping up bug fixed

  - simplified source file name handling

  - scroll pane now used to display source files

  - technically unlimited source files

  - general gui improvement

  - fixed small bugs


* IN 0.3.3

  - Native look and feel for all platforms

  - Improved GUI


* IN 0.3.2

  - multiple file selection, no en/decryption yet


* IN 0.3.1

  - smarter error displaying


* IN 0.3

  - store encrypted file name within file for later use in decryption

  - autodetect original file name from encrypted files

  - fixed README file for proper view with notepad

  - updated wrapping to executable binary for windows

  - now up to 700% smaller executables under windows

  - updated higher resolution icon


* IN 0.2.1

  - fixed progress frame that didn't show correctly

  - updated the about frame

  - fixed the selected file label

  - inclusion of icon and logo


* IN 0.2

  - fixed bug that didn't allow to encrypt/decrypt big sized files

  - added a progress window

  - added a cancel button

  - smarter error handling

  - program now stays open after operation is done

  - encryption/decryption is now performed in a separate thread

  - program packaging changed into a .zip file

  - windows executable .exe compilation added to the program packaging

  - release notes now updated and with more important information available

  - first major release: OhCrypt goes BETA


* IN 0.1.2

  - fixed file name label

  - main window now non-resizable


* IN 0.1.1

  - created a new menu bar with a single option

  - created the "about" panel with the author's name and a link to the sourceforge project page


* IN OLDER VERSIONS

  - updated project page

  - first version released

  - release notes

  - new designed icons

  - screenshots uploaded


INSTALLATION
------------

OhCrypt does not require any installation, simply mark it as executable and run it. For Windows users open the the .jar file with a java virtual machine. Other platforms execute .jar file using preferred JVM. It only requires a java virtual machine to be installed on the host machine. It also runs under all platforms.


CONTACT
-------

For any questions related to the program or its developer, email: omtinez@gmail.com


TO-DO
-----

- progress bar set accordingly with true progress

- implement twofish and serpent algorithms

- create solid documentation for ohcrypt

- create project's webpage
