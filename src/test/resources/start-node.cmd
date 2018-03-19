SET PATH=%JAVA_HOME%\bin;%PATH%;
java ^
	-Dwebdriver.chrome.driver="chromedriver.exe" ^
	-Dwebdriver.gecko.driver="geckodriver.exe" ^
	-Dwebdriver.ie.driver="IEDriverServer.exe" ^
	-jar selenium-server-standalone-3.11.0.jar ^
	-role node ^
	-hub http://localhost:4444/grid/register ^
	-port 5555
