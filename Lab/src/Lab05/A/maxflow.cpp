#include <iostream>
#include <vector>
#include <fstream>
#include <memory>

const int INF = 2000000000;
using namespace std;

struct graph_data {
    shared_ptr<vector<vector<int>>> graph;
    shared_ptr<vector<vector<int>>> residual_net;
    int max_cap;

    graph_data(shared_ptr<vector<vector<int>>> const &graph, shared_ptr<vector<vector<int>>> const &residual_net, int max_cap) :
            graph(graph), residual_net(residual_net), max_cap(max_cap) { }
};

int max(int a, int b);

int min(int a, int b);

graph_data read_graph(ifstream &in, unsigned int n, unsigned int m);

int dfs(int from, int to, int current_flow, shared_ptr<vector<vector<int>>> graph, shared_ptr<vector<vector<int>>> residual_net, shared_ptr<vector<bool>> visited, int min_cap);

long long max_flow(unsigned int n, shared_ptr<vector<vector<int>>> graph, shared_ptr<vector<vector<int>>> residual_net, int max_cap);

int main() {
    ifstream in("maxflow.in");
    ofstream out("maxflow.out");
    unsigned int n, m;
    in >> n >> m;
    auto data = read_graph(in, n, m);
    auto graph = data.graph;
    auto residual_net = data.residual_net;
    int max_cap = data.max_cap;
    out << max_flow(n, graph, residual_net, max_cap) << endl;
    return 0;
}

int max(int a, int b) {
    return a > b ? a : b;
}

int min(int a, int b) {
    return a < b ? a : b;
}

graph_data read_graph(ifstream &in, unsigned int n, unsigned int m) {
    shared_ptr<vector<vector<int>>> graph(new vector<vector<int>>(n));
    shared_ptr<vector<vector<int>>> residual_net(new vector<vector<int>>(n, vector<int>(n, 0)));
    int max_cap = 0;
    for (int i = 0; i < m; i++) {
        int a, b, cap;
        in >> a >> b >> cap;
        a--;
        b--;
        (*graph)[a].push_back(b);
        (*graph)[b].push_back(a);
        (*residual_net)[a][b] = cap;
        max_cap = max(max_cap, cap);
    }
    return graph_data(graph, residual_net, max_cap);
}

long long max_flow(unsigned int n, shared_ptr<vector<vector<int>>> graph, shared_ptr<vector<vector<int>>> residual_net, int max_cap) {
    int min_cap = 1;
    while (min_cap * 2 <= max_cap) {
        min_cap *= 2;
    }
    long long flow = 0;
    while (min_cap > 0) {
        int r;
        do {
            shared_ptr<vector<bool>> visited(new vector<bool>(n, false));
            r = dfs(0, n - 1, INF, graph, residual_net, visited, min_cap);
            flow += r;
        } while (r > 0);
        min_cap /= 2;
    };
    return flow;
}

int dfs(int from, int to, int current_flow, shared_ptr<vector<vector<int>>> graph, shared_ptr<vector<vector<int>>> residual_net, shared_ptr<vector<bool>> visited, int min_cap) {
    if (from == to) {
        return current_flow;
    }
    if ((*visited)[from]) {
        return 0;
    }
    (*visited)[from] = true;
    for (int v : (*graph)[from]) {
        if ((*residual_net)[from][v] >= min_cap) {
            int d = dfs(v, to, min(current_flow, (*residual_net)[from][v]), graph, residual_net, visited, min_cap);
            if (d > 0) {
                (*residual_net)[from][v] -= d;
                (*residual_net)[v][from] += d;
                return d;
            }
        }
    }
    return 0;
};