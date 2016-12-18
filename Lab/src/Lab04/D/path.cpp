#include <fstream>
#include <climits>
#include <vector>
#include <tuple>

using namespace std;

const long long INF = LLONG_MAX / 4;

vector<tuple<unsigned int, unsigned int, long long>> *read_graph(ifstream &in, unsigned int &n, unsigned int &m, unsigned int &s);

vector<long long> *ford_bellman(vector<tuple<unsigned int, unsigned int, long long>> *graph, unsigned int n, unsigned int s);

void print_answer(ofstream &out, vector<long long> *dist, unsigned int n);

int main() {
    ifstream in("path.in");
    ofstream out("path.out");
    unsigned int n, m, s;
    vector<tuple<unsigned int, unsigned int, long long>> *graph = read_graph(in, n, m, s);
    vector<long long> *dist = ford_bellman(graph, n, s);
    print_answer(out, dist, n);
    delete graph;
    delete dist;
    return 0;
}

vector<tuple<unsigned int, unsigned int, long long>> *read_graph(ifstream &in, unsigned int &n, unsigned int &m, unsigned int &s) {
    vector<tuple<unsigned int, unsigned int, long long>> *graph = new vector<tuple<unsigned int, unsigned int, long long>>;
    in >> n >> m >> s;
    --s;
    for (unsigned int i = 0; i < m; i++) {
        unsigned int u, v;
        long long w;
        in >> u >> v >> w;
        --u;
        --v;
        (*graph).emplace_back(u, v, w);
    }
    return graph;
}

vector<long long> *ford_bellman(vector<tuple<unsigned int, unsigned int, long long>> *graph, unsigned int n, unsigned int s) {
    vector<long long> *dist = new vector<long long>(n, INF);
    (*dist)[s] = 0;
    for (unsigned int i = 0; i < n; i++) {
        for (tuple<unsigned int, unsigned int, long long> e : *graph) {
            unsigned int u, v;
            long long w;
            tie(u, v, w) = e;
            if ((*dist)[u] == -INF) {
                (*dist)[v] = -INF;
            } else if ((*dist)[u] != INF && (*dist)[v] != -INF && (*dist)[v] > (*dist)[u] + w) {
                (*dist)[v] = (*dist)[u] + w;
                if ((*dist)[v] < -INF) {
                    (*dist)[v] = -INF;
                }
            }
        }
    }
    for (unsigned int i = 0; i < 2 * n; i++) {
        for (tuple<unsigned int, unsigned int, long long> e : *graph) {
            unsigned int u, v;
            long long w;
            tie(u, v, w) = e;
            if ((*dist)[u] != INF && (*dist)[v] > (*dist)[u] + w) {
                (*dist)[v] = -INF;
            }
        }
    }
    return dist;
}

void print_answer(ofstream &out, vector<long long> *dist, unsigned int n) {
    for (int i = 0; i < n; i++) {
        if ((*dist)[i] == INF) {
            out << '*';
        } else if ((*dist)[i] == -INF) {
            out << '-';
        } else {
            out << (*dist)[i];
        }
        out << endl;
    }
}