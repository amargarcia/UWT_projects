# Author: AJ Garcia

import hashlib
import hmac
import os
import re
from io import open
from typing import Tuple

# global vars
userFirst = ""
userLast = ""
userInt1 = ""
userInt2 = ""
numSum = ""
numProd = ""
inputFileText = ""
userOutputFile = ""
userPassword = ""
salt = ""
pw_hash = ""


# calls all functions
def main():
    firstName()
    lastName()
    getInts()
    password()
    inputFile()
    outputFile()
    writeToFile()


# gets user first name
def firstName():
    global userFirst
    print("""
** Enter your first name. **
    - Name should be no more than 50 characters.
    - First name should start with an uppercase letter.
        Prompt will continue until correct format is supplied. """)
    userFirst = "a"
    while not verifyName(userFirst):
        userFirst = input("-> Enter first name: ")
        if verifyName(userFirst):
            print("First name accepted.")
        else:
            print("!! Invalid !!")
    return userFirst


# gets user last name
def lastName():
    global userLast
    print("""
** Enter your last name. **
    - Name should be no more than 50 characters.
    - Last name should start with an uppercase letter.
    - Hyphenated last names are not accepted.
        Prompt will continue until correct format is supplied.""")
    userLast = "a"
    while not verifyName(userLast):
        userLast = input("-> Enter last name: ")
        if verifyName(userLast):
            print("Last name accepted.")
        else:
            print("!! Invalid !!")
    return userLast


# gets user ints, sums them, gets their product, and checks to make sure it is within int range
def getInts():
    global userInt1, userInt2, numProd, numSum
    while not verifyInt(numSum) or not verifyInt(numProd):
        print("""
** Enter first int. **
    - Int should be between -2,147,483,648 and 2,147,483,647.
        Prompt will continue until correct format is supplied.""")
        while not verifyInt(userInt1):
            userInt1 = input("-> Enter first integer: ")
            if verifyInt(userInt1):
                print("Integer accepted.")
            else:
                print("!! Invalid !!")

        print("""
** Enter second int. **
    - Int should be between -2,147,483,648 and 2,147,483,647.
    - Ints should stay within the specified range when adding AND multiplying the two ints.
        Prompt will continue until correct format is supplied.""")
        while not verifyInt(userInt2):
            userInt2 = input("-> Enter second integer: ")
            if verifyInt(userInt2):
                print("Integer accepted.")
            else:
                print("!! Invalid !!")

        numSum = int(userInt1) + int(userInt2)
        numProd = int(userInt1) * int(userInt2)

        if verifyInt(numSum) and verifyInt(numProd):
            print("Integers accepted.")
        else:
            print("Selected integers cause overflow.")
            userInt1 = ""
            userInt2 = ""


# stores requested input file and checks to makes sure file exists before storing its data
def inputFile():
    global inputFileText
    filePath = ""
    print("""
** Enter the absolute path of the input file name. **
    - File names must start with a letter.
    - File names can contain letters and numbers.
    - Backslashes and spaces are not accepted.
    - Accepted file extensions are .txt and .doc.
    - File names will be at least one letter and no more than 15 total characters.
    - Paths should not require root access.
        Prompt will continue until correct format is supplied.""")
    while True:
        filePath = input("-> Enter file path: ")
        if verifyInFileType(filePath):
            print("Format accepted")
            try:
                with open(filePath, 'r') as f:
                    inputFileText = f.read()
                    f.close()
                    break
            except:
                print("File not found.")
        else:
            print("Invalid file path.")


# gets requested filename to save all user data to
def outputFile():
    global userOutputFile
    print("""
** Enter the output file name. **
    - File names must start with a letter.
    - File names can contain letters and numbers.
    - Accepted file extension is .txt.
    - File names will be at least one letter and no more than 15 total characters.
        Prompt will continue until correct format is supplied.""")

    while not verifyOutFileType(userOutputFile):
        userOutputFile = input("-> Enter output file name: ")
        if not verifyOutFileType(userOutputFile):
            print("!! Invalid !!")
    print("File name accepted.")
    return userOutputFile


# writes all data to outputFile
def writeToFile():
    try:
        fileToWrite = open(userOutputFile, "x")
        fileToWrite.write("User's name: " + userFirst + " " + userLast + "\n")
        fileToWrite.write("First int: " + userInt1 + "\n")
        fileToWrite.write("Second int: " + userInt2 + "\n")
        fileToWrite.write("Sum of two ints: " + str(numSum) + "\n")
        fileToWrite.write("Product of two ints: " + str(numProd) + "\n")
        fileToWrite.write("Contents of input file: " + inputFileText)
        fileToWrite.close()
    except:
        print("Unable to write to file")


# gets user password, salts it, hashes it, then stores the hash to a file
def password():
    global userPassword, salt, pw_hash
    print("""
** Enter a password. **
    - Password should be at least 8 characters and max of 15.
    - Password should contain at least one uppercase, one lowercase, and one special character ?!.@#$%^&()
    \tPrompt will continue until correct format is supplied. """)

    while True:
        while not verifyPassword(userPassword):
            userPassword = input("-> Enter a password: ")
            if verifyPassword(userPassword):
                salt, pw_hash = hashPassword(userPassword)
                userPassword = ""
                userPassword = input("-> Verify password: ")
                if not checkPassword(salt, pw_hash, userPassword):
                    print("Passwords do not match.\n")
                    userPassword = ""
                else:
                    print("Passwords match.")
                    return
            else:
                print("!! Invalid format !!")


# makes sure name starts with a capital letter and is 1-50 characters
def verifyName(name):
    return re.match("^[A-Z][a-z]{0,49}$", name)


# ensures int is in range
def verifyInt(num):
    try:
        realInt = int(num)
        return 2147483647 >= realInt >= -2147483648
    except:
        return False


# ensures file path is absolute path starting with root directory and ends with txt extension
def verifyInFileType(fileType):
    return re.match("^[A-Z]:[/\\d\\w]*?/[A-Za-z]([A-Za-z0-9]){1,14}.(txt)$", fileType)


# ensures file name starts with a letter, is no more than 15 characters and has a txt file extension
def verifyOutFileType(fileType):
    return re.match("^[A-Za-z]([A-Za-z0-9]){1,14}.(txt)$", fileType)


# ensures password is 8-15 characters and contains a specific set of special characters, an uppercase
# letter, a lower case letter, and a number.
def verifyPassword(pw):
    return re.match("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[?!.@#$%^&()])[A-Za-z\\d?!.@#$%^&()]{8,15}$", pw)


# hashes password with random salt
def hashPassword(pw: str) -> Tuple[bytes, bytes]:
    salt = os.urandom(16)
    pw_hash = hashlib.pbkdf2_hmac('sha256', pw.encode(), salt, 100000)
    return salt, pw_hash


# compares two hashes
def checkPassword(salt: bytes, pw_hash: bytes, pw: str) -> bool:
    return hmac.compare_digest(pw_hash, hashlib.pbkdf2_hmac('sha256', pw.encode(), salt, 100000))


# starts program
main()