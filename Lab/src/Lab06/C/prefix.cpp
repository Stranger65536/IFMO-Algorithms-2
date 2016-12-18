#include <fstream>
#include <vector>
#include <memory>
#include <string>

using namespace std;

shared_ptr<vector<int>> prefix_function(string &s);

void print_result(ostream &out, shared_ptr<vector<int>> result);

int main() {
    ifstream in("prefix.in");
    ofstream out("prefix.out");
    string s;
    in >> s;
    print_result(out, prefix_function(s));
    return 0;
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

void print_result(ostream &out, shared_ptr<vector<int>> result) {
    auto it = result->cbegin();
    while (it != result->cend()) {
        out << *(it++) << " ";
    }
    out << endl;
}