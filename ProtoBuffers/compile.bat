:sub_1
set /p src=Input file:
:sub_0 
protoc --java_out=comp %src%
@echo off
setlocal
:PROMPT
SET /P AREYOUSURE=Should we retry?(Y/[N])
IF /I "%AREYOUSURE%" NEQ "Y" GOTO ENDO

goto:sub_0
:ENDO
@echo off
setlocal
:PROMPT
SET /P AREYOUSURE=Try another file?(Y/[N])
IF /I "%AREYOUSURE%" NEQ "Y" GOTO END

goto:sub_1

:END
endlocal