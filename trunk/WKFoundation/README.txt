WKFoundation framework
======================
This is a collection of basic java functionality that is commonly used and useful.

Dependencies
=============
Included jars, including, but not limited to
	backport-util-concurrent (used to get java 1.5 threading features in java 1.4 apps)
	ostermillerutils (for CSV file and data processing)
	
Installed jars/frameworks
	ERJars (from Project Wonder, at time of writing, has log4j plus others)
	JavaFoundation (from Apple - the com.webobjects.foundation classes)
	
It is important to keep ths project's dependencies such that the resulting framework (and its jars) 
can be used for command line java apps without pulling in other huge frameworks.
It is intended that this framework be a generic all-purpose swiss army knife

So, be cautious about adding new 3rd party jars to this 
and be double-cautious about adding Framework dependencies!

Kieran 1/16/2007
	