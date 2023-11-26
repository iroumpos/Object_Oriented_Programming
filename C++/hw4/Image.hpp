#ifndef IMAGE_HPP
#define IMAGE_HPP

#include <iostream>
#include "Pixel.hpp"

using namespace std;

class Image {
    protected:
        int width;
        int height;
        int max_luminocity;
    public:
        int getWidth() const { return width; }
        int getHeight() const { return height; }
        int getMaxLuminocity() const { return max_luminocity; }
        void setWidth(int width) { this->width = width; }
        void setHeight(int height) { this->height = height; }
        void setMaxLuminocity(int lum) {this->max_luminocity = lum; }

        virtual Image& operator += (int times) = 0;
        virtual Image& operator *= (double factor) = 0;
        virtual Image& operator !() = 0;
        virtual Image& operator ~() = 0;
        virtual Image& operator *() = 0;
        virtual ~Image() {};

        virtual Pixel& getPixel(int row, int col) const = 0;
        friend std::ostream& operator << (std::ostream& out, Image& image);
        
        int calc_y(int red,int green, int blue) {return ((66 * red + 129 * green + 25 * blue + 128) >> 8) + 16;}
        int calc_u(int red,int green, int blue) {return ((-38 * red - 74 * green + 112 * blue + 128) >> 8) + 128;}
        int calc_v(int red,int green, int blue) {return ((112 * red - 94 * green - 18 * blue + 128) >> 8) + 128;}
        int clip(int value) {
            if(value < 0){
                return 0;
            }
            if(value > 255) {
                return 255;
            } 
            return value;
        }
        int calc_r(int y, int u, int v) {
            int c = y - 16;
            int e = v - 128;
            return clip((298*c + 409*e + 128) >> 8);
        }
        int calc_g(int y, int u, int v) {
            int c = y - 16;
            int d = u - 128;
            int e = v - 128;
            return clip((298*c - 100*d - 208*e + 128) >> 8);
        }
        int calc_b(int y, int u, int v) {
            int c = y - 16;
            int d = u - 128;
            return clip((298*c + 516*d + 128) >> 8);
        }
        int grey_to_y(int value) {
            int c = value - 16;
            return clip((298*c + 128) >> 8);
        }

};

#endif