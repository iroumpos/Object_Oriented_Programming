#ifndef TOKEN_HPP
#define TOKEN_HPP

#include "Image.hpp"

class Token {
    private:
        string name;
        Image* ptr;
    public:
        Token(const string& ="", Image* = nullptr);
        ~Token() {
            delete ptr;
        }
        string getName() const;
        Image* getPtr() const;
        void setName(const string& );
        void setPtr(Image* ptr);
};


#endif