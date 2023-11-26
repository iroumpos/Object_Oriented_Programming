#include <iostream>
#include <sstream>
#include <fstream>
#include <string>
#include <vector>
#include <algorithm>
#include <sys/stat.h>

#include "Image.hpp"
#include "RGBImage.hpp"
#include "GSCImage.hpp"
#include "Token.hpp"

using namespace std;

Image* readNetpbmImage(const char* filename) {
    ifstream f(filename);
    if(!f.is_open()) {
        std::cout << "[ERROR] Unable to open " << filename << std::endl;  
    }
    Image* img_ptr = nullptr;
    string type;
    
    if(f.good() && !f.eof())
        f >> type;
    if(!type.compare("P3")) {
        img_ptr = new RGBImage(f);
    }
    else if(!type.compare("P2")) {
        img_ptr = new GSCImage(f);
    }
    else if(f.is_open()) {
        std::cout << "[ERROR] Invalid file format" << std::endl;
    }
    
    return img_ptr;
}



std::ostream& operator << (std::ostream& out, Image& image) {
    if (const RGBImage* rgbimg = dynamic_cast<const RGBImage*>(&image)) {
        out << "P3" << endl;
        out << rgbimg->getWidth() << " " << rgbimg->getHeight() << " " << rgbimg->getMaxLuminocity() << endl;

        for (int row = 0; row < rgbimg->getHeight(); row++) {
            for (int col = 0; col < rgbimg->getWidth(); col++) {
                RGBPixel pxl = rgbimg->color_img[row][col];
                out << static_cast<int>(pxl.getRed()) << " ";
                out << static_cast<int>(pxl.getGreen()) << " ";
                out << static_cast<int>(pxl.getBlue());
                out << endl;
            }

            
        }
    }

    if (const GSCImage* gscimg = dynamic_cast<const GSCImage*>(&image)) {
        out << "P2" << endl;
        out << gscimg->getWidth() << " " << gscimg->getHeight() << " " << gscimg->getMaxLuminocity() << endl;

        for (int row = 0; row < gscimg->getHeight(); row++) {
            for (int col = 0; col < gscimg->getWidth(); col++) {
                GSCPixel pxl = gscimg->bw_pixel[row][col];
                out << static_cast<int>(pxl.getValue());
                out << endl;
            }

            
        }
    }

    return out;
}


vector<Token*> database;           // Database as a vector

bool token_exists(string token) {
    for (Token* tok : database) {
        if (tok && tok->getName() == token) {
            return true;
        }
    }
    return false;
}


void remove_element(string token,int flag) {
    if (flag == 0) {
        auto it = remove_if(database.begin(), database.end(), [&token](Token* tok) {
            return tok->getName() == token;
        });

        if (it != database.end()) {
            delete *it;
            database.erase(it);
        }
    } else {
        auto it = remove_if(database.begin(), database.end(), [&token](Token* tok) {
            if  (tok->getName() == token) {
                delete tok;
                return true;
            }
            return false;
        });

        if (it != database.end()) {
            database.erase(it, database.end());
        }

    }
}



Token* find_img(const string tokenName) {
    for (Token* tok : database) {
        if (tok->getName() == tokenName) {
            return tok;
        }
    }
    return nullptr;
}

bool fileExists(const std::string& filename) {
    struct stat buffer;
    return (stat(filename.c_str(), &buffer) == 0);
}

void exportToFile(const string tokenName, const string filename) {
    Token* tknptr = nullptr;
    for (Token* tok : database) {
        if (tok->getName() == tokenName) {
            tknptr = tok;
            break;
        }
    }

    Image* ptr = tknptr->getPtr();

    ofstream outputFile(filename);
    outputFile << *ptr;
    outputFile.close();

}

int main() {
    
    while(1) {
        char option;
        string input;
        
        Token* tok;
        Image* img_ptr;
        
        Image* up_grey;
        const RGBImage* c_img;
        const GSCImage* grey;


        // cout << "$> ";
        getline(cin, input);

        option = input[0];

        // break the input
        istringstream iss(input);
        string word;
        vector<string> wordTable;
       
        
        
        while(getline(iss,word,' ')) {
            wordTable.push_back(word);
        }
        
        switch (option) {
            case 'i':
                if(wordTable[2] != "as" || wordTable[3].at(0) != '$') {
                    cout << "\n-- Invalid command! --" << endl;
                    break;
                }
                if(database.size() > 0 && token_exists(wordTable[3])) {
                    cout << "[ERROR] Token " << wordTable[3] << " exists" << endl;
                    break;
                }
                img_ptr = readNetpbmImage(wordTable[1].c_str());
                if(img_ptr == nullptr){
                    break;
                } 
                
                tok = new Token(wordTable[3],img_ptr);
                database.push_back(tok);
                cout << "[OK] Import " << wordTable[3] << endl;
                break;

            case 'e':
                if(wordTable[2] != "as" || wordTable[1].at(0) != '$') {
                    cout << "\n-- Invalid command! --" << endl;
                    break;
                }
                if(!token_exists(wordTable[1])){
                    cout << "[ERROR] Token " << wordTable[1] << " not found!" << endl;
                    break;
                }
                
                if(fileExists(wordTable[3])) {
                    cout << "[ERROR] File exists" << endl;
                    break;
                }
                exportToFile(wordTable[1], wordTable[3]);
                cout << "[OK] Export " << wordTable[1] << endl;

                break;
            case 'd':
                if(wordTable[1].at(0) != '$') {
                    cout << "\n-- Invalid command! --" << endl;
                    break;
                }
                if(!token_exists(wordTable[1])){
                    cout << "[ERROR] Token " << wordTable[1] << " not found!" << endl;
                    break;
                }
                remove_element(wordTable[1],1);
                cout << "[OK] Delete " << wordTable[1] << endl;
                break;
            case 'n':
                if(wordTable[1].at(0) != '$') {
                    cout << "\n-- Invalid command! --" << endl;
                    break;
                }

                if(!token_exists(wordTable[1])){
                    cout << "[ERROR] Token " << wordTable[1] << " not found!" << endl;
                    break;
                }

                img_ptr = (find_img(wordTable[1])->getPtr());
                (*img_ptr).operator!();
                cout << "[OK] Color Inversion " << wordTable[1] << endl;
                break;

            case 'z':
                if(wordTable[1].at(0) != '$') {
                    cout << "\n-- Invalid command! --" << endl;
                    break;
                }

                if(!token_exists(wordTable[1])){
                    cout << "[ERROR] Token " << wordTable[1] << " not found!" << endl;
                    break;
                }

                img_ptr = (find_img(wordTable[1])->getPtr());
                (*img_ptr).operator~();
                cout << "[OK] Equalize " << wordTable[1] << endl;
                break;
            case 'm':
                if(wordTable[1].at(0) != '$') {
                    cout << "\n-- Invalid command! --" << endl;
                    break;
                }

                if(!token_exists(wordTable[1])){
                    cout << "[ERROR] Token " << wordTable[1] << " not found!" << endl;
                    break;
                }
                
                img_ptr = (find_img(wordTable[1])->getPtr());
                (*img_ptr).operator*();
                cout << "[OK] Mirror " << wordTable[1] << endl;

                break;
            case 'g':
                if(wordTable[1].at(0) != '$') {
                    cout << "\n-- Invalid command! --" << endl;
                    break;
                }

                if(!token_exists(wordTable[1])){
                    cout << "[ERROR] Token " << wordTable[1] << " not found!" << endl;
                    break;
                }

                img_ptr = (find_img(wordTable[1])->getPtr());
                if (dynamic_cast<const GSCImage*>(img_ptr)) {
                    cout << "[NOP] Already grayscale " << wordTable[1] << endl;
                    break;
                }
                
                c_img = dynamic_cast<const RGBImage*>(img_ptr);
                

                grey = new GSCImage(*c_img);
                

                up_grey = const_cast<Image*>(static_cast<const Image*>(grey));


                remove_element(wordTable[1],1);
                database.push_back(new Token(wordTable[1], up_grey));
                cout << "[OK] Grayscale " << wordTable[1] << endl;

                break;

            case 's':
                if(wordTable[2] != "by" || wordTable[1].at(0) != '$') {
                    cout << "\n-- Invalid command! --" << endl;
                    break;
                }

                if(!token_exists(wordTable[1])){
                    cout << "[ERROR] Token " << wordTable[1] << " not found!" << endl;
                    break;
                }

                img_ptr = (find_img(wordTable[1])->getPtr());
                *img_ptr *= stod(wordTable[3]);

                cout << "[OK] Scale " << wordTable[1] << endl;

                break;
            case 'r':
                if(wordTable[4] != "times" || wordTable[2] != "clockwise" || wordTable[1].at(0) != '$') {
                    cout << "\n-- Invalid command! --" << endl;
                    break;
                }

                if(!token_exists(wordTable[1])){
                    cout << "[ERROR] Token " << wordTable[1] << " not found!" << endl;
                    break;
                }

                img_ptr = (find_img(wordTable[1])->getPtr());
                *img_ptr += stod(wordTable[3]);
                
                
                cout << "[OK] Rotate " << wordTable[1] << endl;
                break;
            case 'q':
                //Here you delete the memory
                
                for(Token* tok: database){
                    //remove_element(tok->getName());
                    delete tok;
                }
                
                database.clear();
                return -1;

        }
    }
}