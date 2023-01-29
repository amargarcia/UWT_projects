/** ***************************************************************************
  @file         MyShell.c
  @author       AJ Garcia

*******************************************************************************/

/**
DONOT change the existing function definitions. You can add functions, if necessary.
*/



/**
  @brief Fork a child to execute the command using execvp. The parent should wait for the child to terminate
  @param args Null terminated list of arguments (including program).
  @return returns 1, to continue execution and 0 to terminate the MyShell prompt.
 */

#include <stdio.h>
#include <sys/wait.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

#define MAX_ARGS 100
#define BUF 255


int execute(char **args)
{

	bool programRun = true;

	if (strcmp(args[0], "") == 0) {
		// blank
	} else if (strcmp(args[0], "exit") == 0) {
		printf("exiting");
		programRun = false;
	
	}  else {
		int rc = fork();
		if (rc > 0) {
			wait(NULL);
		} else if (rc == 0) {
			execvp(args[0], args);
			printf("error executing command: No such file or directory \n");
			exit(0);
		} else {
			printf("Fork failed \n");
			exit(0);
		}
	}
	free(args);
	return programRun;
}


/**
  @brief gets the input from the prompt and splits it into tokens. Prepares the arguments for execvp
  @return returns char** args to be used by execvp
 */
char** parse(void)
{

	char userInput[BUF];
	char *token;
	char **args = (char**) malloc(MAX_ARGS * sizeof(char));;

	if (args != NULL) {
		for (int i = 0; i < MAX_ARGS; i++) {
			args[i] = (char *) malloc(BUF * sizeof(char));
		}
	}

	printf("MyShell>");
	
	fgets(userInput, BUF, stdin);

	if (userInput[0]  == '\n') {
		strcpy(args[0], "");
	} else {
		strcpy(args[0], strtok(userInput, " \n"));
		int nullCheck = 1;
		while((token = strtok(NULL, " \n")) != NULL) {
			strcpy(args[nullCheck], token);
			nullCheck++;
		}

		if (nullCheck > 0) {
			args[nullCheck] = '\0';
		}
	}

	return args;
}


/**
   @brief Main function should run infinitely until terminated manually using CTRL+C or typing in the exit command
   It should call the parse() and execute() functions
   @param argc Argument count.
   @param argv Argument vector.
   @return status code
 */
int main(int argc, char **argv)
{

	char **args;

	do {
		args = parse();
	} while(execute(args));

      	return EXIT_SUCCESS;
}
