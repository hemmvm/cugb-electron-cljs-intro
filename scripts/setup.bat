@echo off

pushd "%~dp0\.."

echo. & echo Installing node dependencies...
call npm install --no-optional

echo. & echo Installing grunt...
call npm install -g grunt-cli

call grunt setup

echo. & echo setup complete.

popd
