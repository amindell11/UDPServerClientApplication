set /p src=Input file:
:sub_0 
protoc --java_out=comp %src%
@echo off
setlocal
:PROMPT
SET /P AREYOUSURE=Should we retry?(Y/[N])
IF /I "%AREYOUSURE%" NEQ "Y" GOTO END

goto:sub_0

:END
endlocal