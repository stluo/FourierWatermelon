# FourierWatermelon

<img src="https://github.com/stluo/FourierWatermelon/blob/master/23555423_10212906989342806_1946067151_o.png" width="400"><img src="https://github.com/stluo/FourierWatermelon/blob/master/23558050_10212907160747091_1647842561_o.png" width="400">



##What is the problem you’re solving? 
How to ensure that the fruits you buy will be fresh and tasty. 

##Who are users and/or customers? 
This app targets everyone who eat fruits! (So everyone except college students). 
Can also target grocery stores who want to only keep the best produce on its shelf. 

##What’s currently missing that they need and which your solution provides?
Our app provides EASY and Proven way of judging if a watermelon is sweet or juice. Currently, there are many myths and weird things people do to try and judge watermelons but our app eliminates the guesswork out of buying watermelons. Furthermore, due to the KNN machine learning model we have to process data, we can tailor results for the individual for juicier or sweeter watermelons. 

##So how does it work? 
The key to success is based on 3 key points: 

1. Intuitive, easy to use, and great looking user interface.
- Our simple interface has 3 buttons only reveal itself when they are needed. Output results are displayed nice and big, and more importantly easy to understand. We designed this for your grandmother! 

2. Fourier transforms, Fourier transforms, Fourier transforms!
The Fourier transform is the most important mathematical tool in this century. “Its ubiquity in nearly every field of engineering and physical sciences, all for different reasons, makes it all the harder to narrow down a reason.” A great chunk of what we know about the world is due to using Fourier transforms. Simple examples being how music is recorded and played back, how imagines are saved, all digital and analog communication, etc 

In layman's terms, a Fourier transform transforms a continuous signal such as sound to a finite list of numbers which can accurately represent this signal. It is only then can we actually process the information. 


3. Machine Learning 
After transforming the audio data into a fourier series by using the fourier transform we input the data into our K Nearest Neighbor (KNN) machine learning model. This model will give use the Euclidean distance between each input data set. This way we can say with a percentage that this watermelon will but X% sweet and X% juicy. This model is embedded within the app, no need for an internet connection. It will also prompt the user to rate it a thumbs up or a thumbs down. This input is saved and incorporated back into our model. How machine learning works are the more data the more accurate it is. This way as time goes the model will be biased towards the individual's own tastes for what a good watermelon is. 


##Detailed Analysis 
This app is based on the research findings of "An intelligent procedure for watermelon ripeness detection based on vibration signals" featured in the highly regarded Journal of Food Science and Technology." A quote from the abstract is: "The experimental results showed that the usage of the FFT amplitude of the vibration signals gave the maximum classification accuracy. This method allowed identification at a 95.0 % level of efficiency. Hence, the proposed method can reliably detect watermelon ripeness." The full paper can be viewed: https://www.ncbi.nlm.nih.gov/pmc/articles/PMC4325046/

Other supporting research publications:
https://www.researchgate.net/publication/257458277_Classifying_watermelon_ripeness_by_analysing_acoustic_signals_using_mobile_devices

http://vric.ucdavis.edu/pdf/MELON/watermelon.pdf

