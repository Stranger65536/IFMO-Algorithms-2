#include <fstream>
#include <climits>
#include <vector>
#include <iterator>
#include <set>
#include <forward_list>

using namespace std;

const int INF = INT_MAX / 4;

vector<forward_list<pair<unsigned int, unsigned int>>> *read_graph(ifstream &in, unsigned int &n, unsigned int &m);

vector<unsigned int> *dijkstra(vector<forward_list<pair<unsigned int, unsigned int>>> *graph, unsigned int n);

void print_answer(ofstream &out, vector<unsigned int> *dist);

int main() {
    ifstream in("pathbgep.in");
    ofstream out("pathbgep.out");
    unsigned int n, m;
    vector<forward_list<pair<unsigned int, unsigned int>>> *graph = read_graph(in, n, m);
    vector<unsigned int> *dist = dijkstra(graph, n);
    print_answer(out, dist);
    delete graph;
    delete dist;
    return 0;
}

vector<forward_list<pair<unsigned int, unsigned int>>> *read_graph(ifstream &in, unsigned int &n, unsigned int &m) {
    in >> n >> m;
    vector<forward_list<pair<unsigned int, unsigned int>>> *graph = new vector<forward_list<pair<unsigned int, unsigned int>>>;
    graph->resize(n);
    for (unsigned int i = 0; i < m; i++) {
        unsigned int from, to, w;
        in >> from >> to >> w;
        --from;
        --to;
        (*graph)[from].push_front({w, to});
        (*graph)[to].push_front({w, from});
    }
    return graph;
}

vector<unsigned int> *dijkstra(vector<forward_list<pair<unsigned int, unsigned int>>> *graph, unsigned int n) {
    vector<unsigned int> *dist = new vector<unsigned int>(n, INF);
    (*dist)[0] = 0;
    set<pair<unsigned int, unsigned int>> q;
    q.insert({(*dist)[0], 0});
    while (!q.empty()) {
        unsigned int v = q.begin()->second;
        q.erase(q.begin());
        for (auto edge : (*graph)[v]) {
            unsigned int to = edge.second;
            unsigned int w = edge.first;
            if ((*dist)[v] + w < (*dist)[to]) {
                q.erase({(*dist)[to], to});
                (*dist)[to] = (*dist)[v] + w;
                q.insert({(*dist)[to], to});
            }
        }
    }
    return dist;
}

void print_answer(ofstream &out, vector<unsigned int> *dist) {
    copy((*dist).begin(), (*dist).end(), ostream_iterator<unsigned int>(out, " "));
}