#include <stdio.h>
#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>

#define NUMTHREADS 4

void *threadfn(void *ptr) {
  int threadnum = (int) ptr;
  sleep(rand() % 2); // Sleep 0 or 1 seconds
  printf("Thread %d\n", threadnum);
  return 0;
}

int main(void) {
  sranddev(); // seed random number generator
  pthread_t threads[NUMTHREADS];  // Thread IDs

  for (int i = 0; i < NUMTHREADS; i++) pthread_create(&threads[i], 0, threadfn, (void *)i);
  for (int i = 0; i < NUMTHREADS; i++) pthread_join(threads[i], 0);
  return 0;
}
