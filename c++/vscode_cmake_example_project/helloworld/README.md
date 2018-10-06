# A minimal example project

Demonstrates building in vscode with cmake and debugging

## How to build

Step 1:
```
cd helloworld
cmake -H. -Bbuild -DCMAKE_INSTALL_PREFIX:PATH=/tmp/foo
```

Step 2: Build in VSCode (Ctrl+Shift+B) or build manually like this:
```
cd build
cmake --build .
./build/HelloWorld
```

### Visual Studio Code config files
```
.vscode/tasks.json - Config for building in vscode (Ctrl+Shift+B)
.vscode/launch.json - Config for debugging in vscode (F5)
```
