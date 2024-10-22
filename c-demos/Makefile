all:	d1_interleaving d2_increment d3_atomic
	./d1_interleaving
	./d2_increment
	./d3_atomic

d1_interleaving:	d1_interleaving.c
	gcc -Wno-int-to-pointer-cast -Wno-pointer-to-int-cast -o d1_interleaving d1_interleaving.c -lpthread

d2_increment:	d2_increment.c
	gcc -o d2_increment d2_increment.c -lpthread

d3_atomic:	d3_atomic.c
	# Uses GCC-specific built-in atomic function
	gcc -o d3_atomic d3_atomic.c -lpthread
