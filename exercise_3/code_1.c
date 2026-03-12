#include <stdio.h>
#include <unistd.h>

int main() {
	int i;
	int N = 10;

	for (i = 0; i < N; i++) {
		fork();
	}
	sleep(10);
	return 0;
}