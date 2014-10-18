# README

SimpleJdbcPool (aka sjpool) is a fast and lightweight Jdbc pool.  
It is fast (if not the fastest), thanks to excellent concurrency API added to the JDK since version 5.  
It is light (if not the lightest, 5K jar) and it doesn't require any external dependencies.  
It should work with any Java version (5+).

Features are minimal (if not minimalist !) :

* size configurations (min and max number of connections).
* growing size control.
* drop connections idled for a certain time.

Some benchmarks :

1. 1 threads requesting 1,000,000 connections : 500 ms.
2. 100 threads requesting 10,000 connections : 500 ms.
3. 500 threads requesting 10,000 connections : 1500 ms.
4. 500 threads requesting 100 connections with a delay of 10 ms before closing the connection : 6000 ms (note that there is )
5. 20 threads requesting 2,000 connections (pool size min/max = 10/10) : 30 ms.

Todo :

1. Unit tests !
2. Make use (or not !) of user/password
3. Better handling of idling connection (timeouts)
4. ...