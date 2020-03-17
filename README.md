# FuzzYourYelon
Advanced URL Fuzzer made in java. It allows the user to scan a website, get all links on it and then scan those links until it can find no more. This allows to understand the folder structure of a website and, who knows, some nice stuff ;).
I am not responsible for the use (or misuse) of this tool, I just did it for fun.

# Usage
Download the latest `FuzzYourYelon.jar` from [here](https://github.com/lonyelon/FuzzYourYelon/releases). Then, to execute the software type:
```
java -jar FuzzYourYelon.jar
```

## Installation
To install it on Linux you can do the following:
1. Download the file as before.
2. Copy it to `bin` with `sudo cp FuzzYourYelon.jar /bin/`.
3. `sudo echo "#!/bin/bash\njava -jar FuzzYourYelon.jar" > /bin/fyy`.
Now you can just use the command `fyy` to use the tool! 

# Future plans
- [x] Scan hypertext documents.
- [x] Save directories.
- [x] Add some kind of progress bar.
- [ ] Brute force url.
- [x] Console interface.
- [ ] Load help from file.
- [ ] Extra console functions.
  - [ ] Clone directories.
  - [ ] Save files
- [ ] Graphical user interface.

# Credits
2020, LonYelon
