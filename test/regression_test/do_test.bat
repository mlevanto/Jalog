
java -cp ..\..\src Jalog %1.pro > %1.ans
fc %1.ans %1.ref
REM fc %1.ans %1.pro
if ERRORLEVEL 1 (
  echo %1 : Errorlevel %ERRORLEVEL%
  set /A jalog_errors=%jalog_errors%+1
) else (
REM
)
echo ----------
