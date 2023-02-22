Assignment 2: Parallel Matrix Multiplier
Objective
The purpose of this assignment is to implement a multi-threaded C program that uses a shared bounded
buffer to coordinate the production of NxM matrices for consumption in matrix multiplication. For two
matrices M1 and M2 to be multiplied, the number of columns of M1 must equal the number of rows of
M2. The program will perform parallel work using multiple threads to: (1) produce NxM matrices and
place them into a shared buffer, and (2) consume NxM matrices from the bounded buffer for pairing
with another matrix for matrix multiplication having a valid number of rows and columns. Matrices
consumed from the bounded buffer with an invalid number of elements for multiplication are discarded
and the buffer is queried again to obtain a new candidate matrix for multiplication.
Starter code (pcmatrix.tar.gz in Canvas) is provided to help jumpstart implementing the parallel matrix
multiplier with the synchronized bounded buffer. The goal of the project is to focus on synchronization
and pthreads, not implementing matrix functions and operations as this code is already provided.
Producer algorithm:
One or more producer threads work together to produce “NUMBER_OF_MATRICES” (default value is
LOOPS defined in pcmatrix.h) # of matrices and place them in the shared bounded buffer. The producer
should call Matrix * GenMatrixRandom() (refer to matrix.h and matrix.c) to generate a NxM matrix
where the number of rows and columns is random between 1 and 4 (i.e., DEFAULT_MATRIX_MODE 0).
Consumer algorithm:
One or more consumer threads work together to perform matrix multiplication. Each consumer thread
gets a matrix from the bounded buffer (M1) at first. Then the consumer thread gets a second matrix
from the bounded buffer (M2). Calling the matrix.c routine Matrix * MatrixMultiply(Matrix * m1,
Matrix * m2) will return a pointer with a result of the matrix multiplication (M3), or a NULL if matrix
multiplication fails due to a mismatch of the number of elements. If a NULL is received, then the
consumer thread discards the matrix (i.e., M2) and memory is free’d by calling void FreeMatrix(Matrix *
mat) (refer to matrix.h and matrix.c). The consumer thread then grabs the next available matrix from
the bounded buffer as M2. When a valid matrix M2 is found that pairs with M1, the matrix
multiplication operation is performed and the result in M3 is printed using the void
DisplayMatrix(Matrix * mat, FILE *stream) routine (refer to matrix.h and matrix.c). With a successful
multiplication, both M1 and M2 will be free’d and a new M1 will be obtained.
NOTE: it is important to let a producer to finish if no more matrices need to be produced and let a
consumer to finish if no more matrices are available and free a potential M1 obtained already. [HINT:
use a synchronized global counter to track if “NUMBER_OF_MATRICES” has been produced or
consumed in various places. The counter can be parsed to each thread as an argument to be used.]
TCSS 422 Operating Systems Instructor: Juhua Hu
2
Starter Code:
1.The following modules are provided:
Module Header file Source file Description
Counter counter.h counter.c Synchronized counter data structure
Matrix matrix.h matrix.c Matrix helper routines
Prodcons prodcons.h prodcons.c Producer Consumer worker thread module
Pcmatrix pcmatrix.h pcmatrix.c Program main module with int main()
A Makefile is provided to compile the modules into a pcMatrix binary.
An initial demonstration of the random matrix generation routine, matrix multiplication, and matrix
display is provided in pcmatrix.c int main(). The matrix multiplication output format should be followed
for the actual program implementation.
2.The following constant parameters and global values are defined in pcmatrix.h:
DEFAULTS
NUMWORK DEFAULT number of producer and consumer worker threads.
OUTPUT Integer true (1) / false (0) to enable or disable debug output. See matrix.c
for example use of #if OUTPUT / #endif.
MAX DEFAULT size of the bounded buffer defined as an array of Matrix struct
ptrs.
LOOPS DEFAULT number of matrices to produce/consume.
DEFAULT_MATRIX_MODE DEFAULT type of matrices to produce
GLOBALS
BOUNDED_BUFFER_SIZE Global variable defining the bounded buffer size.
NUMBER_OF_MATRICES Global variable defining the number of matrices to produce/consume.
MATRIX_MODE Defines the type of matrices to produce. (0=random, 1-n=fixed row/col)
Note that the number of worker threads is handled using a local variable called numw in pcmatrix.c.
Code is included in pcmatrix.c to load command line arguments into the global variables for the user.
Dynamically sizing the bounded buffer is already done. The matrix_mode is also already implemented in
matrix.c. If no command line arguments are provided, the default values are used. A message is
displayed indicating the parameterization:
Otherwise the command line arguments.
TCSS 422 Operating Systems Instructor: Juhua Hu
3
It should be possible to pass different values for these parameters as command line arguments to invoke
your program differently for testing purposes.
3.The following data types are provided:
Struct Defined in File Description
counter_t counter.h Synchronized shared counter
counters_t counter.h Shared structure with a producer and consumer counter.
Matrix matrix.h Matrix structure that tracks the number of rows and cols and
includes a pointer to an MxN integer matrix.
ProdConsStats prodcons.h Structure that tracks the number of matrices produced or
consumed, as well as the sum of all matrices produced or
consumed, and the number of matrices multiplied.
The program uses the ProdConsStats struct in prodcons.h to track:
sumtotal The sum of all elements of matrices produced or consumed.
multtotal The total number of matrices multiplied by consumer threads.
matrixtotal The total number of matrices produced by producer threads, or consumed by
consumer threads.
[HINT] This struct can be defined locally in each consumer and producer thread and used to track the
work of the thread, i.e., each one is associated with its own struct to record the statistics. Later, this can
be returned to the main thread from ‘pthread_join’. Then, the main thread is responsible for adding up
the cumulative work to print out a summary of the total work. The total number of matrices produced
and that of consumed must equal [a good way to track if your program is working correctly]. The sum of
all elements produced and that of consumed must equal.
Program Testing Recommendations
For testing correctness of concurrent programming, try out different sizes of the bounded buffer
(BOUNDED_BUFFER_SIZE). If the bounded buffer is too large, this could minimize errors, and hide
possible concurrency problems. The grader will reduce from default value of MAX to a low setting to
quickly expose flaws. Similarly, only producing and consuming a very small number of matrices
(NUMBER_OF_MATRICES) will hide concurrency problems. Testing your program with a large number
for matrices (NUMBER_OF_MATRICES) also can help expose concurrency problems.
Starting Out
As a starting point for this assignment, the final implementation of the producer/consumer is exactly
what we need as a basic version. Moreover, inspect the signal.c example discussed during the same
lecture. This provides a working matrix generator which uses locks and conditions to synchronize
generation of 1 matrix at a time to a shared bounded buffer of 1 defined as int ** bigmatrix;. A producer
thread example is provided as the worker routine void *worker(void *arg), and the consumer thread
code is implemented inside of int main. You will need to implement the consumer thread as a separate
worker in this assignment. The signal.c example program stores matrices in a bounded buffer of 1. In
TCSS 422 Operating Systems Instructor: Juhua Hu
4
this assignment, the bounded buffer becomes an array of Matrix struct pointers: Matrix ** bigmatrix, as
shown in the demo code of pcmatrix.c.
Development Tasks
The following is a list of development tasks for this assignment.
Task 1- Implement a bounded buffer. This will be a buffer of pointers to Matrix structs (records). The
datatype should be “Matrix ** bigmatrix”, and the bounded buffer will be limited to
BOUNDED_BUFFER_SIZE size. Note: the demo code has it in the pcmatrix.c and similar idea can be
borrowed.
Task 2 – Implement get() and put() routines for the bounded buffer.
Task 3 – Call put() from within prod_worker() and add all necessary uses of mutex locks, condition
variables, and signals. Integrate the counters.
Task 4 – Call get() from within cons_worker() and all necessary uses of mutex locks, condition variables,
and signals. Integrate the counters. Implement the matrix multiplication by consuming matrices from
the bounded buffer as described above.
Task 5 – Create one producer pthread and one consumer pthread in pcmatrix.c to launch the parallel
matrix production and multiplication.
Tasks 6- Once a 1 producer and 1 consumer version of the program is working correctly, refactor
pcmatrix.c to use an array of producer threads, and an array of consumer threads. The array size is
numw. (Extra credit for correct implementation of 3 or more producer/consumer pthreads).
Points to consider:
1. A concurrent shared bounded buffer will store matrices for potential multiplication. The use of
signals is required to inform consumer threads when there are matrices available to consume,
and to signal the producer when there is available space in the bounded buffer to add more
matrices. For testing, we might change the size of the bounded buffer to a low number, for
example 2, to ensure your program still works.
2. Put() will add a matrix to the end of the bounded buffer. Get() retrieves a matrix from the other
end. With multiple producers and consumers, multiple matrices can be added and removed for
multiplication from the shared bounded buffer simultaneously. You’ll need to ensure that no
two consumers consume the same matrix.
3. This program will require the use of both locks (mutexes) and condition variables.
4. Memory for matrices should be freed once a matrix is consumed to prevent a memory leak.
Without releasing memory, generating millions of matrices will place severe demands on the
program’s memory heap. 
