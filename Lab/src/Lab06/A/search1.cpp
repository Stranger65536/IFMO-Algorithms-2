#include <fstream>
#include <vector>
#include <memory>
#include <string>

using namespace std;

pair<int, shared_ptr<vector<int>>> get_all_occurences(string &s, string &pattern);

void print_result(ostream &out, pair<int, shared_ptr<vector<int>>> result) ;

int main() {
    ifstream in("search1.in");
    ofstream out("search1.out");
    string s, pattern;
    in >> pattern >> s;
    print_result(out, get_all_occurences(s, pattern));
    return 0;
}

pair<int, shared_ptr<vector<int>>> get_all_occurences(string &s, string &pattern) {
    unsigned int count = 0;
    int last_pos = -1;
    shared_ptr<vector<int>> occurences(new vector<int>());
    while ((last_pos = s.find(pattern, (unsigned int) (last_pos + 1))) != string::npos) {
        occurences->push_back(last_pos + 1);
        count++;
    }
    return make_pair(count, occurences);
}

void print_result(ostream &out, pair<int, shared_ptr<vector<int>>> result) {
    out << result.first << endl;
    auto it = result.second->cbegin();
    while (it != result.second->cend()) {
            out << *(it++) << " ";
    }
    out << endl;
}