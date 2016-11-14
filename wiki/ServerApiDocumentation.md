# Server API Documentation

## General Information

### Based Host

	http://traffic.hcmut.edu.vn/

### API List

API  |
-----|
User.Login |
User.Logout |
User.GetUserInfo |
TripDetails.CreateTrip |
TripDetails.GetListTrip |
TripDetails.GetListSharedTrip |
TripDetails.LikeTrip|
TripDetails.GetCountLikeTrip |
TripDetails.SaveCommentOnTrip|
TripDetails.GetListTripComment|
TripDetails.CreatePointOnTrip |
TripDetails.GetListPointOnTrip |
TripDetails.UploadImageAtPoint |
TripDetails.ShareTrip |

## HTTP API Details

### User

#### User.Login

### TripDetails

#### TripDetails.SaveCommentOnTrip

Post a omment on a specific trip

** Request **

	POST /ITS/rest/comment/SaveCommentOnTrip

Field | Description
-- | --
tokenId | Login Token ID
tripId	| Trip ID
content | Comment content

Example:

	{
		"tokenId": "ed16a4cd-1627-43c1-a6b3-9b3b925742ea",
		"tripId" : "317e5cc8-b2e4-48e5-93e3-8efaa4a02cfe",
		"content": "a0"
	}
	
	
** Response **

Field | Description
-- | --
description | Description
code	| Result code

	{
	  "description": "Insert comment on trip successful!",
	  "code": "1"
	}