#ifndef RGBIMAGE_HPP
#define RGBIMAGE_HPP

#include "Image.hpp"
#include "RGBPixel.hpp"

class RGBImage : public Image {
    private:
        RGBPixel** color_img;       // colored image
    
    
    public:
        RGBImage(); 
        RGBImage(const RGBImage& img);
        RGBImage(std::istream& stream);
        ~RGBImage() override {
            for (int i = 0; i < height; i++) {
                delete[] color_img[i];
            }
            delete[] color_img;
        }

        RGBPixel** getImage() const;
        RGBImage& operator = (const RGBImage& img);
        
        virtual Image& operator += (int ) override ;
        // RGBImage operator + (int ) ;
        
        virtual Image& operator *= (double factor) override;
        virtual Image& operator !() override;
        virtual Image& operator ~() override;
        virtual Image& operator *() override;
        
        virtual Pixel& getPixel(int row, int col) const override;
        friend std::ostream& operator << (std::ostream& out, Image& image);


};



#endif