# Android AWS Serverless Project - A Restaurant Server App (Server Side) Originated by EDMT Dev / Re-implement By Alan Tang Sing Lun 

## Table of Contents

* [OverView](#OverView)
* [Setup Environment](#Setting-Environment)
* [Deploy Back-End Serverless](#Deploy-BackEnd-Serverless)
* [Installation Front-End](#Installation-FrontEnd)
* [Configuration Serverless](#Configuration-Serverless)
* [Configuration Front-End](#Configurations-FrontEnd)
* [Run the App](#Run-App)
* [Remove the Serverless](#Remove-Serverless)
* [Project Reference Sources Or Links](#references)

## OverView

* Declare Of Statement
Please Note this is a continous development App. Some Functions and coding transformation are not yet complete. 

In this project, we will apply the skills we have acquired in this cloud engineering course from Udacity and Udemy to design, deploy and operate a AWS Serverless Android Restaurant App. The App consist of two parts, a backend which is written in Nodejs and enitirely a `Labmda` serverless architecture. We will use the `Serverless` framework to deploy our Lambda fucntions. And the other part is the Android App which is written in Java. The main use of this App is to allow users to login to the system and upload the Food menu to different different categories of Cusine. User requires to insert a title, description, price, discount and the photo of the particular food to the Category it belongs. The uploaded photos will be uploaded to the S3 Bucket and downloaded with a pre-signed URL. User can also delete or update the food items.

For this project we will uses Window 10 Professional as the developement and production environment.

## Setting-Environment

* AWS Configurations
    Before deploying the Serverless, you need to make some security configurations on the AWS console.
    * Please Grant Full Access for the User.
        * AmazonS3FullAccess
        * AmazonDynamoDBFullAccess
        * AdministratorAccess
        * AmazonAPIGatewayAdministrator
        * AWSCloudFormationFullAccess

* Install Nodejs. 
    when you sucessfully install Nodejs, `npm (Node Package Manager)` will be installed as well.
    * To install Nodjs, please follow this link: https://nodejs.org/en/download/

* Install git 
    * npm install git-all

* Install Serverless FrameWork 
    * npm install -g serverless
    * Config Serverless FrameWork. Type in the following command 
        * `serverless config credentials --provider aws --key AKIAIOSFODNN7EXAMPLE --secret wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY`
          Please substitute the --key and --secret with you own values.

* Clone the Project
    * Type in the following command to clone the Project.
      `git clone https://github.com/tangsinglun/AWS-SERVERLESS-ANDROID-RESTAURANT-APP.git`  

And that is it! We have setup an environment for the Restaurant App. Next we will config the project for the Production Mode and deploy it to the AWS.

## Deploy-BackEnd-Serverless

Please follow the below procedures for deploying the backend serverless.

*  Install All the Dependencies
    * Browser to the location `Serverlerss`
    * Type in the following command `npm install`. Wait till the installation completes
*  Deploy the App to the AWS.
    * Browser to the location `Serverless`
    * Type in the following command `sls deploy -v`. Wait till the deployment completes. 

## Installation-FrontEnd

*  As the Android App is not launched at the Android PlayStore. We need to run it on an Android Simulator. 
    * Please install the Android Studio. From the google search type in `Android Studio`, then it will direct you to the installation page. 
    * Run the Android Studio and open the folder `Android`.
    * Wait till the Gradle Synchronization Completes.
    * You can create and setup the phone simulator from the `AVD Manager` console.

## Configurations-Serverless

* There is nothing much to configure, just need to rename the S3 Bucket to your desire name. GOTO `serverless.yml` file on line 30 to rename it.    

## Configuration-FrontEnd

* After Deploying the Serverless Backend Successfully, You will be notify the restAPI for each of the requested Path. Please copy the `RestAPI ID` and change it in the Andorid Restaurant App.
  The loction is in the `java\website.programming.androideatitserver\Common\common`, change it at the variable `AWS_SERVER_API`.

## Run-App

*  For Android 

   You can run the App by clicking the `Play` Button.

   Enjoy :)

## Remove-Serverless

*  PLEASE MOST IMPORTANT REMOVE ALL THE IMAGES FROM THE `serverless-alan-eat-restaurant-dev` BEFORE CONTINUE.
*  From the location Serverless. Type `sls remove`.  

## References

* Code References
    * [Udacity](https://www.udacity.com/)
    * [udemy](https://www.udemy.com/)
    * [GitHub](https://github.com/)
    * [Serverless](https://serverless.com/)
    * [JSON Web Tokens](https://jwt.io/)
    * [EDMT Dev YouTube Channel](https://www.youtube.com/channel/UCllewj2bGdqB8U9Ld15INAg)
    * [Joe Parys Udemy Course](https://www.udemy.com/course/android-development-course/)

    
