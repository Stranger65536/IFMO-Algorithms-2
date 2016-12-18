#include <fstream>
#include <vector>
#include <algorithm>
#include <memory>

const int INF = 2000000000;
using namespace std;

struct graph_data {
    shared_ptr<vector<vector<int>>> graph;
    shared_ptr<vector<vector<int>>> residual_net;
    shared_ptr<vector<vector<int>>> min;
    int max_cap;

    graph_data(shared_ptr<vector<vector<int>>> const &graph, shared_ptr<vector<vector<int>>> const &residual_net, shared_ptr<vector<vector<int>>> const &min, int max_cap) :
            graph(graph), residual_net(residual_net), min(min), max_cap(max_cap) { }
};

graph_data read_graph(ifstream &in, unsigned int n, unsigned int m);

int dfs(int from, int to, int cur_flow, shared_ptr<vector<vector<int>>> graph, shared_ptr<vector<bool>> visited, shared_ptr<vector<vector<int>>> residual_net, int min_cap);

void circ(unsigned int n, int max_cap, shared_ptr<vector<vector<int>>> graph, shared_ptr<vector<vector<int>>> residual_net);

void print_answer(ostream &out, unsigned int n, unsigned int m, shared_ptr<vector<vector<int>>> graph, shared_ptr<vector<vector<int>>> residual_net, shared_ptr<vector<vector<int>>> min);

int main() {
    ifstream in("circulation.in");
    ofstream out("circulation.out");
    unsigned int n, m;
    in >> n >> m;
    n += 2;
    auto data = read_graph(in, n, m);
    auto graph = data.graph;
    auto min = data.min;
    auto residual_net = data.residual_net;
    int max_cap = data.max_cap;
    circ(n, max_cap, graph, residual_net);
    print_answer(out, n, m, graph, residual_net, min);
    return 0;
}

graph_data read_graph(ifstream &in, unsigned int n, unsigned int m) {
    shared_ptr<vector<vector<int>>> graph(new vector<vector<int>>(n));
    shared_ptr<vector<vector<int>>> residual_net(new vector<vector<int>>(n, vector<int>(n, 0)));
    shared_ptr<vector<vector<int>>> min(new vector<vector<int>>(n, vector<int>(n, 0)));
    int max_cap = 0;
    for (int i = 0; i < m; i++) {
        int u, v;
        in >> u >> v;
        int l, c;
        in >> l >> c;
        c -= l;
        (*min)[u][v] = l;
        (*graph)[u].push_back(v);
        (*graph)[v].push_back(u);
        (*residual_net)[u][v] = c;
        (*graph)[0].push_back(v);
        (*graph)[v].push_back(0);
        (*residual_net)[0][v] += l;
        (*graph)[n - 1].push_back(u);
        (*graph)[u].push_back(n - 1);
        (*residual_net)[u][n - 1] += l;
        max_cap = max(max_cap, c);
        max_cap = max(max_cap, l);
    }
    return graph_data(graph, residual_net, min, max_cap);
}

void circ(unsigned int n, int max_cap, shared_ptr<vector<vector<int>>> graph, shared_ptr<vector<vector<int>>> residual_net) {
    int min_cap = 1;
    shared_ptr<vector<bool>> visited(new vector<bool>(n, false));
    while (min_cap * 2 <= max_cap) {
        min_cap *= 2;
    }
    while (min_cap > 0) {
        int r;
        do {
            fill(visited->begin(), visited->end(), false);
            r = dfs(0, n - 1, INF, graph, visited, residual_net, min_cap);
        } while (r > 0);

        min_cap /= 2;
    };
}

void print_answer(ostream &out, unsigned int n, unsigned int m, shared_ptr<vector<vector<int>>> graph, shared_ptr<vector<vector<int>>> residual_net, shared_ptr<vector<vector<int>>> min) {
    for (int v : (*graph)[0]) {
        if ((*residual_net)[0][v] > 0) {
            out << "NO" << endl;
            return;
        }
    }
    out << "YES" << endl;
    for (int i = 0; i < m; i++) {
        out << (*min)[(*graph)[n - 1][i]][(*graph)[0][i]] + (*residual_net)[(*graph)[0][i]][(*graph)[n - 1][i]] << endl;
    }
}

int dfs(int from, int to, int cur_flow, shared_ptr<vector<vector<int>>> graph, shared_ptr<vector<bool>> visited, shared_ptr<vector<vector<int>>> residual_net, int min_cap) {
    if (from == to) {
        return cur_flow;
    }
    if ((*visited)[from]) {
        return 0;
    }
    (*visited)[from] = true;
    for (int v : (*graph)[from]) {
        if ((*residual_net)[from][v] >= min_cap) {
            int d = dfs(v, to, min(cur_flow, (*residual_net)[from][v]), graph, visited, residual_net, min_cap);
            if (d > 0) {
                (*residual_net)[from][v] -= d;
                (*residual_net)[v][from] += d;
                return d;
            }
        }
    }
    return 0;
};