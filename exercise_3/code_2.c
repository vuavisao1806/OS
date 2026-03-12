#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

const char NODES_PATH[] = "nodes.txt";

int main() {
	int i;
	int N = 10;

	pid_t root_pid = getpid();
	FILE* node_inp = fopen(NODES_PATH, "w");
	fclose(node_inp);

	for (i = 0; i < N; i++) {
		fork();
	}

	node_inp = fopen(NODES_PATH, "a");
	fprintf(node_inp, "%d\n", getpid());
	fclose(node_inp);

	while (wait(NULL) > 0);

	if (getpid() == root_pid) {
		int num_nodes = 0;
		FILE* node_out = fopen(NODES_PATH, "r");
		char line[256];

		while (fgets(line, sizeof(line), node_out)) ++num_nodes;
		printf("The number of nodes in the tree of processes: %d\n", num_nodes);
	}
	return 0;
}