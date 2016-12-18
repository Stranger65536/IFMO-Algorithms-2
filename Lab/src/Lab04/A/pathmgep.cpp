#include <fstream>
#include <climits>
#include <vector>

using namespace std;

const int INF = INT_MAX / 4;

vector<vector<int>> *read_graph(ifstream &in, unsigned int &n, unsigned int &src, unsigned int &target);

vector<int> *dijkstra(vector<vector<int>> *graph, unsigned int n, unsigned int src);

void print_answer(ofstream &out, vector<int> *dist, unsigned int &target);

int main() {
    ifstream in("pathsg.in");
    ofstream out("pathmgep.out");
    unsigned int n, src, target;
    vector<vector<int>> *graph = read_graph(in, n, src, target);
    vector<int> *dist = dijkstra(graph, n, src);
    print_answer(out, dist, target);
    delete graph;
    delete dist;
    return 0;
}

vector<int> *dijkstra(vector<vector<int>> *graph, unsigned int n, unsigned int src) {
    vector<int> *dist = new vector<int>(n, INF);
    vector<bool> used(n, false);
    (*dist)[src] = 0;
    while (true) {
        int minpos = 0;
        int min = INF;
        for (int i = 0; i < n; i++) {
            if (!used[i] && (*dist)[i] < min) {
                minpos = i;
                min = (*dist)[i];
            }
        }
        if (min == INF) {
            break;
        }
        for (int i = 0; i < n; i++) {
            if ((*graph)[minpos][i] > 0 && (*dist)[i] > (*dist)[minpos] + (*graph)[minpos][i]) {
                (*dist)[i] = (*dist)[minpos] + (*graph)[minpos][i];
            }
        }
        used[minpos] = true;
    }
    return dist;
}

vector<vector<int>> *read_graph(ifstream &in, unsigned int &n, unsigned int &src, unsigned int &target) {
    in >> n >> src >> target;
    --src;
    --target;
    vector<vector<int>> *graph = new vector<vector<int>>;
    graph->resize(n, vector<int>(n));
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            in >> (*graph)[i][j];
        }
    }
    return graph;
}

void print_answer(ofstream &out, vector<int> *dist, unsigned int &target) {
    if ((*dist)[target] == INF) {
        out << -1 << endl;
    } else {
        out << (*dist)[target] << endl;
    }
}