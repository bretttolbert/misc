# A minimal example project

Demonstrates building in vscode with cmake and debugging

## How to build

```
cmake -H. -Bbuild -DCMAKE_INSTALL_PREFIX:PATH=/tmp/foo
cd build
cmake --build .
./build/HelloWorld
```

## Visual Studio Code
```
.vscode/tasks.json - Config for building in vscode (Ctrl+Shift+B)
.vscode/launch.json - Config for debugging in vscode (F5)
```