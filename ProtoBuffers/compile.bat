set /p src=Input file: 
protoc --java_out=comp %src%
pause