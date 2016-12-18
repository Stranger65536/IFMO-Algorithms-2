#include <fstream>
#include <climits>
#include <vector>

using namespace std;

const int INF = INT_MAX / 4;

vector<vector<int>> *read_graph(ifstream &in, unsigned int &n, unsigned int &m);

void ford_bellman(vector<vector<int>> *graph, unsigned int n);

void print_answer(ofstream &out, vector<vector<int>> *graph, unsigned int n);

int main() {
    ifstream in("pathbgep.in");
    ofstream out("pathsg.out");
    unsigned int n, m;
    vector<vector<int>> *graph = read_graph(in, n, m);
    ford_bellman(graph, n);
    print_answer(out, graph, n);
    delete graph;
    return 0;
}

void ford_bellman(vector<vector<int>> *graph, unsigned int n) {
    for (unsigned int i = 0; i < n; i++) {
        for (unsigned int u = 0; u < n; u++) {
            for (unsigned int v = 0; v < n; v++) {
                (*graph)[u][v] = (*graph)[u][v] < (*graph)[u][i] + (*graph)[i][v] ? (*graph)[u][v] : (*graph)[u][i] + (*graph)[i][v];
            }
        }
    }
}

vector<vector<int>> *read_graph(ifstream &in, unsigned int &n, unsigned int &m) {
    in >> n >> m;
    vector<vector<int>> *graph = new vector<vector<int>>;
    graph->resize(n, vector<int>(n, INF));
    for (int i = 0; i < n; i++) {
        (*graph)[i][i] = 0;
    }
    for (unsigned int i = 0; i < m; i++) {
        unsigned int from, to, weight;
        in >> from >> to >> weight;
        --from;
        --to;
        (*graph)[from][to] = weight;
    }
    return graph;
}

void print_answer(ofstream &out, vector<vector<int>> *graph, unsigned int n) {
    for (unsigned int u = 0; u < n; u++) {
        for (unsigned int v = 0; v < n; v++) {
            out << (*graph)[u][v] << ' ';
        }
        out << endl;
    }
}