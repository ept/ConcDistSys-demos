#include <stdio.h>
#include <pthread.h>
#include <unistd.h>
#include <stdlib.h>

#define NUMTHREADS 2
#define INCREMENTS 1000000

volatile static int counter = 0;

void *threadfn(void *ptr) {
  for (int i = 0; i < INCREMENTS; i++) counter++;
  return NULL;
}

int main(void) {
  pthread_t threads[NUMTHREADS];  // Thread IDs
  for (int i = 0; i < NUMTHREADS; i++) pthread_create(&threads[i], 0, threadfn, NULL);
  for (int i = 0; i < NUMTHREADS; i++) pthread_join(threads[i], 0);

  printf("Final counter value: %d\n", counter);
  return 0;
}
