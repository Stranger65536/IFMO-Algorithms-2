#include <fstream>
#include <vector>
#include <memory>
#include <string>

using namespace std;

pair<int, shared_ptr<vector<int>>> get_all_occurences(string &s, string &pattern);

void print_result(ostream &out, pair<int, shared_ptr<vector<int>>> result);

shared_ptr<vector<int>> prefix_function(string &s);

int main() {
    ifstream in("search2.in");
    ofstream out("search2.out");
    string s, pattern;
    in >> pattern >> s;
    print_result(out, get_all_occurences(s, pattern));
    return 0;
}

pair<int, shared_ptr<vector<int>>> get_all_occurences(string &s, string &pattern) {
    unsigned int count = 0;
    shared_ptr<vector<int>> occurences(new vector<int>());
    if (pattern.length() == 0) {
        return make_pair(count, occurences);
    }
    string prepared = pattern + "#" + s;
    auto pf = prefix_function(prepared);
    for (int i = 0; i < s.length(); i++) {
        if ((*pf)[pattern.length() + i + 1] == pattern.length()) {
            occurences->push_back(i + 2 - pattern.length());
            count++;
        }
    }
    return make_pair(count, occurences);
}

shared_ptr<vector<int>> prefix_function(string &s) {
    unsigned int n = s.length();
    shared_ptr<vector<int>> pi(new vector<int>(n));
    for (int i = 1; i < n; ++i) {
        int j = (*pi)[i - 1];
        while (j > 0 && s[i] != s[j])
            j = (*pi)[j - 1];
        if (s[i] == s[j]) ++j;
        (*pi)[i] = j;
    }
    return pi;
}

void print_result(ostream &out, pair<int, shared_ptr<vector<int>>> result) {
    out << result.first << endl;
    auto it = result.second->cbegin();
    while (it != result.second->cend()) {
        out << *(it++) << " ";
    }
    out << endl;
}