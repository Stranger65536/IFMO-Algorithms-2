#include <iostream>
#include <vector>
#include <queue>
#include <fstream>
#include <memory>

using namespace std;

const int INF = 1000000000;

struct edge {
    int to, throughput, f, num;

    edge(int b, int d, int e, int g) {
        to = b;
        throughput = d;
        f = e;
        num = g;
    }
};

shared_ptr<vector<vector<edge>>> read_graph(ifstream &in, unsigned int n, unsigned int m);

void dinic(unsigned int n, unsigned int s, shared_ptr<vector<vector<edge>>> graph);

void init(unsigned int n, shared_ptr<vector<int>> p);

bool bfs(unsigned int n, unsigned int s, shared_ptr<vector<vector<edge>>> graph, shared_ptr<vector<int>> level);

int dfs(unsigned int n, int v, int flow, shared_ptr<vector<vector<edge>>> graph, shared_ptr<vector<int>> level, shared_ptr<vector<int>> p);

bool vertex_dec(int v, unsigned int n, shared_ptr<vector<vector<edge>>> graph, shared_ptr<vector<bool>> used, shared_ptr<vector<pair<int, int>>> path);

shared_ptr<vector<vector<int>>> decom(unsigned int n, shared_ptr<vector<vector<edge>>> graph);

int main() {
    ifstream in("decomposition.in");
    ofstream out("decomposition.out");
    unsigned int n, m;
    in >> n >> m;
    auto graph = read_graph(in, n, m);
    unsigned int s = 0;
    dinic(n, s, graph);
    auto ans = decom(n, graph);
    out << (*ans).size() << endl;
    for (int i = 0; i < (*ans).size(); i++) {
        for (int j = 0; j < (*ans)[i].size(); j++) {
            out << (*ans)[i][j] << " ";
        }
        out << endl;
    }
    return 0;
}

shared_ptr<vector<vector<edge>>> read_graph(ifstream &in, unsigned int n, unsigned int m) {
    shared_ptr<vector<vector<edge>>> graph(new vector<vector<edge>>(n));
    for (int i = 0; i < m; i++) {
        int a, b, c;
        in >> a >> b >> c;
        a--;
        b--;
        (*graph)[a].push_back(edge(b, c, 0, i));
        (*graph)[b].push_back(edge(a, 0, 0, -1));
    }
    return graph;
}

void dinic(unsigned int n, unsigned int s, shared_ptr<vector<vector<edge>>> graph) {
    shared_ptr<vector<int>> level(new vector<int>(n));
    shared_ptr<vector<int>> p(new vector<int>(n));
    while (bfs(n, s, graph, level)) {
        init(n, p);
        int tmp = dfs(n, 0, INF, graph, level, p);
        while (tmp > 0) {
            tmp = dfs(n, 0, INF, graph, level, p);
        }
    }
}

void init(unsigned int n, shared_ptr<vector<int>> p) {
    for (int i = 0; i < n; i++) {
        (*p)[i] = 0;
    }
}

bool bfs(unsigned int n, unsigned int s, shared_ptr<vector<vector<edge>>> graph, shared_ptr<vector<int>> level) {
    queue<int> q;
    for (unsigned int i = 0; i < n; i++) {
        (*level)[i] = INF;
    }
    (*level)[s] = 0;
    q.push(s);
    while (!q.empty()) {
        int v = q.front();
        q.pop();
        for (unsigned int i = 0; i < (*graph)[v].size(); i++) {
            if ((*graph)[v][i].f == (*graph)[v][i].throughput) {
                continue;
            }
            int to = (*graph)[v][i].to;
            if ((*level)[to] > (*level)[v] + 1) {
                (*level)[to] = (*level)[v] + 1;
                q.push(to);
            }
        }
    }
    return (*level)[n - 1] != INF;
}

int dfs(unsigned int n, int v, int flow, shared_ptr<vector<vector<edge>>> graph, shared_ptr<vector<int>> level, shared_ptr<vector<int>> p) {
    if (flow == 0) {
        return 0;
    }
    if (v == n - 1) {
        return flow;
    }
    for (int it = (*p)[v]; it < (*graph)[v].size(); it++) {
        int to = (*graph)[v][it].to;
        if ((*level)[to] != (*level)[v] + 1) {
            continue;
        }
        int n_flow = dfs(n, to, min(flow, (*graph)[v][it].throughput - (*graph)[v][it].f), graph, level, p);
        if (n_flow != 0) {
            (*graph)[v][it].f += n_flow;
            for (int j = 0; j < (*graph)[to].size(); j++)
                if ((*graph)[to][j].to == v) {
                    (*graph)[to][j].f -= n_flow;
                    break;
                }
            return n_flow;
        }
        (*p)[v]++;
    }
    return 0;
}

shared_ptr<vector<vector<int>>> decom(unsigned int n, shared_ptr<vector<vector<edge>>> graph) {
    shared_ptr<vector<bool>> used(new vector<bool>(n));
    shared_ptr<vector<pair<int, int>>> path(new vector<pair<int, int>>());
    shared_ptr<vector<vector<int>>> ans(new vector<vector<int>>());
    for (unsigned int i = 0; i < n; i++) {
        (*used)[i] = false;
    }
    for (unsigned int k = 0; k < n; k++) {
        while (vertex_dec(k, n, graph, used, path)) {
            int pos = 0, ver = (*graph)[(*path)[(*path).size() - 1].first][(*path)[(*path).size() - 1].second].to;
            if (ver != n - 1) {
                while ((*path)[pos].first != ver) pos++;
            }
            int max_flow = INF;
            for (int o = pos; o < (*path).size(); o++) {
                max_flow = min(max_flow, (*graph)[(*path)[o].first][(*path)[o].second].f);
            }
            vector<int> parts;
            parts.push_back(max_flow);
            parts.push_back((*path).size() - pos);
            for (int j = pos; j < (*path).size(); j++) {
                parts.push_back((*graph)[(*path)[j].first][(*path)[j].second].num + 1);
                (*graph)[(*path)[j].first][(*path)[j].second].f -= max_flow;
            }
            (*ans).push_back(parts);
            if (!(*path).empty()) {
                (*path).clear();
            }
            for (int j = 0; j < n; j++) {
                (*used)[j] = false;
            }
        }
        if (!(*path).empty()) {
            (*path).clear();
        }
        for (int j = 0; j < n; j++) {
            (*used)[j] = false;
        }
    }
    return ans;
}

bool vertex_dec(int v, unsigned int n, shared_ptr<vector<vector<edge>>> graph, shared_ptr<vector<bool>> used, shared_ptr<vector<pair<int, int>>> path) {
    (*used)[v] = true;
    for (int i = 0; i < (*graph)[v].size(); i++)
        if ((*graph)[v][i].num != -1) {
            if ((*graph)[v][i].f > 0) {
                int to = (*graph)[v][i].to;
                (*path).push_back(make_pair(v, i));
                if (!(*used)[to]) {
                    if (vertex_dec(to, n, graph, used, path)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
    if (v == n - 1) {
        return !(*path).empty();
    }
    return false;
}