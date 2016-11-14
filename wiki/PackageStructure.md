# Package Structure #

##The pacakges##

	vn.edu.hcmut.its.tripmaester
	├── model
	└── services
	    └── its


##The short package descriptions ##

	vn.edu.hcmut.its.tripmaester    : The root package
	├── controller                  : The controller package, which received the request 
	|   └── request                 : Contains all requests of the app
	├── model                       : The models package
	└── services                    : Services package
	    └── its                     : Connection to ITS server services

##The full package descriptions ##


Package | Description 
------- | -----------
`vn.edu.hcmut.its.tripmaester` | The root package.
`vn.edu.hcmut.its.tripmaester.controller` | This package contains the controller center, where is the centralize of gathering all requests in the app. This package also hold interfaces of controllers and request. Note: this is not where a concrete request will be handled. The requests will be delivered to somewhere else (services package) to be done.
`vn.edu.hcmut.its.tripmaester.controller.request` | This package contains all requests. Each request has it own Builder pattern. The request will be push into ControllerCenter, and will be dispatched to its own worker.
`vn.edu.hcmut.its.tripmaester.model` | This package contains model classes.
`vn.edu.hcmut.its.tripmaester.services.its` | This package contains classes which provide connection betwen client and ITS server.