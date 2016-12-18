#include <fstream>
#include <climits>
#include <vector>
#include <set>

using namespace std;

const long long INF = LLONG_MAX / 4;

vector<vector<long long>> *read_graph(ifstream &in, unsigned int &n);

vector<unsigned int> *ford_bellman(vector<vector<long long>> *graph, unsigned int n, unsigned int src, bool &changed, unsigned int &changepos);

vector<unsigned int> *restore_path(vector<unsigned int> *p, unsigned int changepos, unsigned int &i);

void print_answer(ofstream &out, vector<unsigned int> *path, bool changed, unsigned int i);

int main() {
    ifstream in("negcycle.in");
    ofstream out("negcycle.out");
    unsigned int i = 0;
    bool changed;
    unsigned int n;
    unsigned int changepos;
    vector<vector<long long>> *graph = read_graph(in, n);
    vector<unsigned int> *path = ford_bellman(graph, n, 0, changed, changepos);
    if (changed) {
        path = restore_path(path, changepos, i);
    }
    print_answer(out, path, changed, i);
    delete graph;
    delete path;
    return 0;
}

vector<vector<long long>> *read_graph(ifstream &in, unsigned int &n) {
    in >> n;
    vector<vector<long long>> *graph = new vector<vector<long long>>(n, vector<long long>(n));
    for (unsigned int i = 0; i < n; i++) {
        for (unsigned int u = 0; u < n; u++) {
            in >> (*graph)[i][u];
        }
    }
    return graph;
}

vector<unsigned int> *ford_bellman(vector<vector<long long>> *graph, unsigned int n, unsigned int src, bool &changed, unsigned int &changepos) {
    vector<long long> dist(n, INF);
    vector<unsigned int> *p = new vector<unsigned int>(n, -1);
    dist[src] = 0;
    for (unsigned int i = 0; i < n + 1; i++) {
        changed = false;
        for (unsigned int u = 0; u < n; u++) {
            for (unsigned int v = 0; v < n; v++) {
                if (dist[v] > dist[u] + (*graph)[u][v]) {
                    dist[v] = dist[u] + (*graph)[u][v];
                    (*p)[v] = u;
                    changed = true;
                    changepos = v;
                }
            }
        }
        if (!changed) {
            break;
        }
    }
    return p;
}

vector<unsigned int> *restore_path(vector<unsigned int> *p, unsigned int changepos, unsigned int &i) {
    set<int> used;
    vector<unsigned int> *path = new vector<unsigned int>;
    unsigned int v = changepos;
    do {
        path->push_back(v);
        if (used.count(v)) {
            break;
        }
        used.insert(v);
        v = (unsigned int) p->at(v);
    } while (true);
    while ((*path)[i] != v) {
        i++;
    }
    delete p;
    return path;
}

void print_answer(ofstream &out, vector<unsigned int> *path, bool changed, unsigned int i) {
    if (changed) {
        out << "YES" << endl;
        out << path->size() - i << endl;
        for (int j = path->size() - 1; j >= i && j >= 0; j--) {
            out << (*path)[j] + 1 << ' ';
        }
    } else {
        out << "NO" << endl;
    };
}