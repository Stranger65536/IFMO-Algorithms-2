#include <fstream>
#include <vector>
#include <memory>
#include <algorithm>

using namespace std;

shared_ptr<vector<vector<int>>> read_graph(ifstream &in, unsigned int n, unsigned int m);

bool dfs(int v, shared_ptr<vector<bool>> visited, shared_ptr<vector<vector<int>>> graph, shared_ptr<vector<int>> match) ;

int max_matching(unsigned int n, unsigned int k, shared_ptr<vector<vector<int>>> graph) ;

int main() {
    ifstream in("matching.in");
    ofstream out("matching.out");
    unsigned int n, k, m;
    in >> n >> k >> m;
    auto graph = read_graph(in, n, m);
    out << max_matching(n, k, graph) << '\n';
    return 0;
}

shared_ptr<vector<vector<int>>> read_graph(ifstream &in, unsigned int n, unsigned int m) {
    int a, b;
    shared_ptr<vector<vector<int>>> graph(new vector<vector<int>>(n));
    for (int i = 0; i < m; i++) {
        in >> a >> b;
        a--;
        b--;
        (*graph)[a].push_back(b);
    }
    return graph;
}

int max_matching(unsigned int n, unsigned int k, shared_ptr<vector<vector<int>>> graph) {
    shared_ptr<vector<int>> match(new vector<int>(k, -1));
    shared_ptr<vector<bool>> visited(new vector<bool>(n, false));
    int result = 0;
    for (int i = 0; i < n; i++) {
        fill(visited->begin(), visited->end(), false);
        if (dfs(i, visited, graph, match)) {
            result++;
        }
    }
    return result;
}

bool dfs(int v, shared_ptr<vector<bool>> visited, shared_ptr<vector<vector<int>>> graph, shared_ptr<vector<int>> match) {
    if ((*visited)[v]) {
        return false;
    }
    (*visited)[v] = true;
    for (int u : (*graph)[v]) {
        if ((*match)[u] == -1 || dfs((*match)[u], visited, graph, match)) {
            (*match)[u] = v;
            return true;
        }
    }
    return false;
};