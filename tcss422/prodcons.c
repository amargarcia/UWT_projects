/*
 *  prodcons module
 *  Producer Consumer module
 *
 *  Implements routines for the producer consumer module based on
 *  chapter 30, section 2 of Operating Systems: Three Easy Pieces
 *
 *  University of Washington, Tacoma
 *  TCSS 422 - Operating Systems
 */

// Include only libraries for this module
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include "counter.h"
#include "matrix.h"
#include "pcmatrix.h"
#include "prodcons.h"


// Define Locks, Condition variables, and so on here
pthread_mutex_t mtLock1 = PTHREAD_MUTEX_INITIALIZER; 
pthread_mutex_t mtLock2 = PTHREAD_MUTEX_INITIALIZER; 
pthread_cond_t mtCond1 = PTHREAD_COND_INITIALIZER;   
pthread_cond_t mtCond2 = PTHREAD_COND_INITIALIZER; 
 
counter_t prod;
counter_t cons;

volatile int counter = 0;
volatile int temp1 = 0; 
volatile int temp2 = 0; 

// Bounded buffer put() get()
int put(Matrix * value)
{
  pthread_mutex_lock(&mtLock1);
  if (value != NULL) {
    if (counter < BOUNDED_BUFFER_SIZE) {
      bigmatrix[temp1] = value;
      temp2 = temp1;
      temp1 = (temp1 + 1) % BOUNDED_BUFFER_SIZE;  
      counter++;
    }
  }
  pthread_mutex_unlock(&mtLock1);
  return 0;
}

Matrix * get()
{
  Matrix * data;
  pthread_mutex_lock(&mtLock1);
  
  if (counter > 0) {
    data = bigmatrix[temp2];
    temp1 = temp2;
    temp2 = (temp2 - 1) % BOUNDED_BUFFER_SIZE;
    counter--;
  }
  
  pthread_mutex_unlock(&mtLock1);
  
  if (data != NULL) {
	  return data;
  }
  return NULL;
}

// Matrix PRODUCER worker thread
void *prod_worker(void *arg)
{
  ProdConsStats * stats = (ProdConsStats*) arg;
  
  while(get_cnt(&prod) < NUMBER_OF_MATRICES) {
	pthread_mutex_lock(&mtLock2);
	
	while (counter == BOUNDED_BUFFER_SIZE) {
		
	  if(get_cnt(&prod) >= NUMBER_OF_MATRICES) {
	    pthread_cond_signal(&mtCond2);
	    pthread_mutex_unlock(&mtLock2);
	    return 0;
	  }
		
	  pthread_cond_wait(&mtCond1, &mtLock2); 
	}
	
	Matrix * mat = GenMatrixRandom(); 
	
	if(get_cnt(&prod) < NUMBER_OF_MATRICES) { 
	  put(mat);
	  stats -> sumtotal += SumMatrix(mat);
	  stats -> matrixtotal += 1;
	  increment_cnt(&prod);
	  pthread_cond_signal(&mtCond2);
	}
		
	pthread_mutex_unlock(&mtLock2);
  }
  
  pthread_cond_broadcast(&mtCond1);
  
  return 0;
}

// Matrix CONSUMER worker thread
void *cons_worker(void *arg)
{
  ProdConsStats * stats = (ProdConsStats*) arg;
  
  while(get_cnt(&cons) < NUMBER_OF_MATRICES) {
	pthread_mutex_lock(&mtLock2);
	
	while (counter == 0) {
		
	  if(get_cnt(&cons) >= NUMBER_OF_MATRICES) {
		pthread_cond_signal(&mtCond2);
		pthread_mutex_unlock  (&mtLock2);
		
		return 0;
	  }
		
	  pthread_cond_broadcast(&mtCond1);
	  pthread_cond_wait(&mtCond2, &mtLock2); 
	}
		
	Matrix * m1 = get();
	stats->sumtotal += SumMatrix(m1);
	increment_cnt(&cons);
	stats->matrixtotal += 1;
	
	while (counter == 0) {
	  if(get_cnt(&cons) >= NUMBER_OF_MATRICES || m1 == NULL) {
	    pthread_cond_signal(&mtCond2);
	    pthread_mutex_unlock  (&mtLock2);
	  
   	    return 0;
	  }
	  
	  pthread_cond_broadcast(&mtCond1);
	  pthread_cond_wait(&mtCond2, &mtLock2);
	}
	
	Matrix * m2 = NULL;
	Matrix * m3;
	
	do {
	  while (counter == 0) { 
	    if(get_cnt(&cons) >= NUMBER_OF_MATRICES || m2 == NULL) {
	    	pthread_cond_signal(&mtCond2);
	    	pthread_mutex_unlock  (&mtLock2);
			
	    	return 0;
	    }		
	    
	  pthread_cond_broadcast(&mtCond1);
	  pthread_cond_wait(&mtCond2, &mtLock2); 
	  }
	  
	  if(m2 != NULL) {
	   FreeMatrix(m2);
	  }
	  
	  m2 = get();
	  increment_cnt(&cons);
	  stats -> sumtotal += SumMatrix(m2);
	  stats -> matrixtotal += 1;
	  m3 = MatrixMultiply(m1, m2); 
	} while (m3 == NULL);
	
	// print matrices
	DisplayMatrix(m1, stdout);
	printf("\tX\n");
	DisplayMatrix(m2, stdout);
	printf("\t=\n");
	DisplayMatrix(m3, stdout);
	printf("\n");

	// free memory
	FreeMatrix(m3);
	FreeMatrix(m2);
	FreeMatrix(m1);
	
	
	stats -> multtotal += 1;
	pthread_cond_signal(&mtCond1);
	pthread_mutex_unlock(&mtLock2);
  	}
	
	pthread_cond_broadcast(&mtCond2); 
	
	return 0;
}
