import sys
import os
from glob import glob




def convert(name):
	namein = name +"_backup"
	os.system("mv "+name+" "+namein)
	f = open(namein, "r")
	o = open(name,   "w")

	lines = f.readlines()

	BADLINES = xrange(4)
	badlines = -9999999

	for line in lines:
		"""
		if   line[0]=="/" and line[1]=="*" and line[7]=="*" and line[8] == "/":
			newline = line[10:]
		elif line[0]=="/" and line[1]=="*" and line[6]=="*" and line[7] == "/":
			newline = line[9:]
		else:
			newline = line
		
		if "/* Location: " in newline:
			badlines = -1
		if badlines>-99:
			badlines += 1
		if badlines in BADLINES:
			continue
		"""
		if "javax.media.opengl" in line:
			newline = line.replace("javax.media.opengl", "com.jogamp.opengl")
		else:
			newline = line
		o.write(newline)

	o.close()
	f.close()


names = glob("./*.java") + glob("./*/*.java")
for name in names:
	print "Processing file: ", name
	convert(name)
