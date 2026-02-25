#########################################################
##                                    If gradle messes up,                                               ##
##       or IntelliJ IDEA does and you're lazy to use gradle manually,       ##
##                          just use this quick python script.                                    ##
##                                                                                                                        ##
## ------------------------------------------------------------------- ##
##                                                                                                                        ##
## It builds the project into a jar and shows colored output from javac. ##
#########################################################

import subprocess
import colorama
import colored
import os

colorama.just_fix_windows_console()

_print = print
_lastLength = 0

def print(text: str, end: str = "")-> None:
  global _lastLength

  length = len(text)
  diff = max(0, _lastLength - length)

  _print("\r" + text + " "*diff + "\x08"*diff, end=end)
  _lastLength = length

os.chdir("/".join(__file__.replace("\\", "/").split("/")[:-1]))

print("Preparing classpath")

path = r"build\loom-cache\argFiles\runClient"
classpath = [
  os.path.expanduser(
    "~\\.gradle\\caches\\modules-2\\files-2.1\\" +
    "org.jetbrains\\annotations\\25.0.0\\" +
    "c1e6b966db352094ac3bf7f88f3bdef332fe43f0\\" +
    "annotations-25.0.0.jar"
  )
]

with open(path, "r", encoding="utf-8") as f:
  classpath += f.read().split(";")

projectdir = r"src\main\java\pl\olafcio\renewed"
codepaths = [
  projectdir + "/*.java"
]

def scan(path: str) -> None:
  global codepaths

  print("Scanning %s for code" % path)

  files = os.listdir(path)
  for fn in files:
    sub = path + "/" + fn

    if os.path.isdir(sub):
      scan(sub)
    else:
      codepaths.append(sub)

scan(projectdir)
print("Compiling classes")

proc = subprocess.run([
   "javac",
  *codepaths,
   "-cp", ";".join(classpath),
   "-d", r"build\classes",
   "-encoding", "UTF-8",
  "-Xlint:unchecked",
  "--release", "8"
], shell=False, stderr=subprocess.PIPE, text=True, encoding="utf-8")

errLines = proc.stderr.splitlines()
for line in errLines:
  if line.startswith("warning: "):
    print("%swarning: %s" % (colored.fore_rgb(170, 170, 0), colored.fore_rgb(100, 100, 0) + line[9:]), end=colored.Style.RESET + "\n")
  elif ": warning: " in line:
    div = line.split(":")
    print("\n%sfile: %s:%s\n%s" % (
      colored.fore_rgb(0, 170, 170), colored.fore_rgb(0, 100, 100) + div[0] + colored.fore_rgb(80, 80, 80),
      div[1],
      colored.fore_rgb(15, 130, 70) + div[3].lstrip()
    ), end=colored.Style.RESET + "\n")
