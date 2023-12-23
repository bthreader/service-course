/* eslint-disable */
import { TypedDocumentNode as DocumentNode } from '@graphql-typed-document-node/core';
export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
export type MakeEmpty<T extends { [key: string]: unknown }, K extends keyof T> = { [_ in K]?: never };
export type Incremental<T> = T | { [P in keyof T]?: P extends ' $fragmentName' | '__typename' ? T[P] : never };
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: { input: string; output: string; }
  String: { input: string; output: string; }
  Boolean: { input: boolean; output: boolean; }
  Int: { input: number; output: number; }
  Float: { input: number; output: number; }
  /** An RFC-3339 compliant Full Date Scalar */
  Date: { input: any; output: any; }
  /** An Int scalar that must be a positive value */
  PositiveInt: { input: any; output: any; }
  /** A Url scalar */
  Url: { input: any; output: any; }
  _FieldSet: { input: any; output: any; }
};

/**  Bikes */
export type Bike = {
  __typename?: 'Bike';
  groupset: Groupset;
  heroImageUrl?: Maybe<Scalars['Url']['output']>;
  id: Scalars['ID']['output'];
  model: Model;
  size: Scalars['String']['output'];
};

/**  Bike brands */
export type BikeBrand = {
  __typename?: 'BikeBrand';
  models?: Maybe<Array<Model>>;
  name: Scalars['String']['output'];
};

export type BikeConnection = {
  __typename?: 'BikeConnection';
  edges: Array<BikeConnectionEdge>;
  /** TODO can we guarantee non-null here? */
  pageInfo?: Maybe<PageInfo>;
};

export type BikeConnectionEdge = {
  __typename?: 'BikeConnectionEdge';
  cursor: Scalars['String']['output'];
  node: Bike;
};

/**
 * If multiple fields are specified, they will be combined with AND logic. If no fields are
 * specified, the filter will be treated as "match all".
 */
export type BikesFilterInput = {
  /** If specified, return only bikes that are available in the provided date range */
  availableDateRange?: InputMaybe<DateRangeFilterInput>;
  /** If specified, return only bikes with a groupset matching the criteria */
  groupset?: InputMaybe<GroupsetFilterInput>;
  /** If specified, return only bikes whose model matches the criteria */
  model?: InputMaybe<ModelFilterInput>;
  /** If specified, return only bikes with a size that matches the string filter */
  size?: InputMaybe<StringFilterInput>;
};

export type CreateBikeBrandInput = {
  name: Scalars['String']['input'];
};

export type CreateBikeInput = {
  /**
   * The name of the groupset of the bike, a groupset with this name must exist otherwise the
   * mutation will fail and an error will be raised
   */
  groupsetName: Scalars['String']['input'];
  heroImageUrl?: InputMaybe<Scalars['Url']['input']>;
  /**
   * The ID of the model of the bike, a model with this id must exist otherwise the mutation will
   * fail and an error will be raised
   */
  modelId: Scalars['ID']['input'];
  size: Scalars['String']['input'];
};

export type CreateGroupsetInput = {
  brand: GroupsetBrand;
  isElectronic: Scalars['Boolean']['input'];
  name: Scalars['String']['input'];
};

export type CreateModelInput = {
  brandName: Scalars['String']['input'];
  modelYear: Scalars['Int']['input'];
  name: Scalars['String']['input'];
};

export type Cursor = {
  __typename?: 'Cursor';
  cursor?: Maybe<Scalars['String']['output']>;
};

export type CursorInput = {
  cursor?: InputMaybe<Scalars['String']['input']>;
};

export type DateRangeFilterInput = {
  from: Scalars['Date']['input'];
  to: Scalars['Date']['input'];
};

export enum ErrorDetail {
  /**
   * The deadline expired before the operation could complete.
   *
   * For operations that change the state of the system, this error
   * may be returned even if the operation has completed successfully.
   * For example, a successful response from a server could have been
   * delayed long enough for the deadline to expire.
   *
   * HTTP Mapping: 504 Gateway Timeout
   * Error Type: UNAVAILABLE
   */
  DeadlineExceeded = 'DEADLINE_EXCEEDED',
  /**
   * The server detected that the client is exhibiting a behavior that
   * might be generating excessive load.
   *
   * HTTP Mapping: 429 Too Many Requests or 420 Enhance Your Calm
   * Error Type: UNAVAILABLE
   */
  EnhanceYourCalm = 'ENHANCE_YOUR_CALM',
  /**
   * The requested field is not found in the schema.
   *
   * This differs from `NOT_FOUND` in that `NOT_FOUND` should be used when a
   * query is valid, but is unable to return a result (if, for example, a
   * specific video id doesn't exist). `FIELD_NOT_FOUND` is intended to be
   * returned by the server to signify that the requested field is not known to exist.
   * This may be returned in lieu of failing the entire query.
   * See also `PERMISSION_DENIED` for cases where the
   * requested field is invalid only for the given user or class of users.
   *
   * HTTP Mapping: 404 Not Found
   * Error Type: BAD_REQUEST
   */
  FieldNotFound = 'FIELD_NOT_FOUND',
  /**
   * The client specified an invalid argument.
   *
   * Note that this differs from `FAILED_PRECONDITION`.
   * `INVALID_ARGUMENT` indicates arguments that are problematic
   * regardless of the state of the system (e.g., a malformed file name).
   *
   * HTTP Mapping: 400 Bad Request
   * Error Type: BAD_REQUEST
   */
  InvalidArgument = 'INVALID_ARGUMENT',
  /**
   * The provided cursor is not valid.
   *
   * The most common usage for this error is when a client is paginating
   * through a list that uses stateful cursors. In that case, the provided
   * cursor may be expired.
   *
   * HTTP Mapping: 404 Not Found
   * Error Type: NOT_FOUND
   */
  InvalidCursor = 'INVALID_CURSOR',
  /**
   * Unable to perform operation because a required resource is missing.
   *
   * Example: Client is attempting to refresh a list, but the specified
   * list is expired. This requires an action by the client to get a new list.
   *
   * If the user is simply trying GET a resource that is not found,
   * use the NOT_FOUND error type. FAILED_PRECONDITION.MISSING_RESOURCE
   * is to be used particularly when the user is performing an operation
   * that requires a particular resource to exist.
   *
   * HTTP Mapping: 400 Bad Request or 500 Internal Server Error
   * Error Type: FAILED_PRECONDITION
   */
  MissingResource = 'MISSING_RESOURCE',
  /**
   * Service Error.
   *
   * There is a problem with an upstream service.
   *
   * This may be returned if a gateway receives an unknown error from a service
   * or if a service is unreachable.
   * If a request times out which waiting on a response from a service,
   * `DEADLINE_EXCEEDED` may be returned instead.
   * If a service returns a more specific error Type, the specific error Type may
   * be returned instead.
   *
   * HTTP Mapping: 502 Bad Gateway
   * Error Type: UNAVAILABLE
   */
  ServiceError = 'SERVICE_ERROR',
  /**
   * Request failed due to network errors.
   *
   * HTTP Mapping: 503 Unavailable
   * Error Type: UNAVAILABLE
   */
  TcpFailure = 'TCP_FAILURE',
  /**
   * Request throttled based on server concurrency limits.
   *
   * HTTP Mapping: 503 Unavailable
   * Error Type: UNAVAILABLE
   */
  ThrottledConcurrency = 'THROTTLED_CONCURRENCY',
  /**
   * Request throttled based on server CPU limits
   *
   * HTTP Mapping: 503 Unavailable.
   * Error Type: UNAVAILABLE
   */
  ThrottledCpu = 'THROTTLED_CPU',
  /**
   * The operation is not implemented or is not currently supported/enabled.
   *
   * HTTP Mapping: 501 Not Implemented
   * Error Type: BAD_REQUEST
   */
  Unimplemented = 'UNIMPLEMENTED',
  /**
   * Unknown error.
   *
   * This error should only be returned when no other error detail applies.
   * If a client sees an unknown errorDetail, it will be interpreted as UNKNOWN.
   *
   * HTTP Mapping: 500 Internal Server Error
   */
  Unknown = 'UNKNOWN'
}

export enum ErrorType {
  /**
   * Bad Request.
   *
   * There is a problem with the request.
   * Retrying the same request is not likely to succeed.
   * An example would be a query or argument that cannot be deserialized.
   *
   * HTTP Mapping: 400 Bad Request
   */
  BadRequest = 'BAD_REQUEST',
  /**
   * The operation was rejected because the system is not in a state
   * required for the operation's execution.  For example, the directory
   * to be deleted is non-empty, an rmdir operation is applied to
   * a non-directory, etc.
   *
   * Service implementers can use the following guidelines to decide
   * between `FAILED_PRECONDITION` and `UNAVAILABLE`:
   *
   * - Use `UNAVAILABLE` if the client can retry just the failing call.
   * - Use `FAILED_PRECONDITION` if the client should not retry until
   * the system state has been explicitly fixed.  E.g., if an "rmdir"
   *      fails because the directory is non-empty, `FAILED_PRECONDITION`
   * should be returned since the client should not retry unless
   * the files are deleted from the directory.
   *
   * HTTP Mapping: 400 Bad Request or 500 Internal Server Error
   */
  FailedPrecondition = 'FAILED_PRECONDITION',
  /**
   * Internal error.
   *
   * An unexpected internal error was encountered. This means that some
   * invariants expected by the underlying system have been broken.
   * This error code is reserved for serious errors.
   *
   * HTTP Mapping: 500 Internal Server Error
   */
  Internal = 'INTERNAL',
  /**
   * The requested entity was not found.
   *
   * This could apply to a resource that has never existed (e.g. bad resource id),
   * or a resource that no longer exists (e.g. cache expired.)
   *
   * Note to server developers: if a request is denied for an entire class
   * of users, such as gradual feature rollout or undocumented allowlist,
   * `NOT_FOUND` may be used. If a request is denied for some users within
   * a class of users, such as user-based access control, `PERMISSION_DENIED`
   * must be used.
   *
   * HTTP Mapping: 404 Not Found
   */
  NotFound = 'NOT_FOUND',
  /**
   * The caller does not have permission to execute the specified
   * operation.
   *
   * `PERMISSION_DENIED` must not be used for rejections
   * caused by exhausting some resource or quota.
   * `PERMISSION_DENIED` must not be used if the caller
   * cannot be identified (use `UNAUTHENTICATED`
   * instead for those errors).
   *
   * This error Type does not imply the
   * request is valid or the requested entity exists or satisfies
   * other pre-conditions.
   *
   * HTTP Mapping: 403 Forbidden
   */
  PermissionDenied = 'PERMISSION_DENIED',
  /**
   * The request does not have valid authentication credentials.
   *
   * This is intended to be returned only for routes that require
   * authentication.
   *
   * HTTP Mapping: 401 Unauthorized
   */
  Unauthenticated = 'UNAUTHENTICATED',
  /**
   * Currently Unavailable.
   *
   * The service is currently unavailable.  This is most likely a
   * transient condition, which can be corrected by retrying with
   * a backoff.
   *
   * HTTP Mapping: 503 Unavailable
   */
  Unavailable = 'UNAVAILABLE',
  /**
   * Unknown error.
   *
   * For example, this error may be returned when
   * an error code received from another address space belongs to
   * an error space that is not known in this address space.  Also
   * errors raised by APIs that do not return enough error information
   * may be converted to this error.
   *
   * If a client sees an unknown errorType, it will be interpreted as UNKNOWN.
   * Unknown errors MUST NOT trigger any special behavior. These MAY be treated
   * by an implementation as being equivalent to INTERNAL.
   *
   * When possible, a more specific error should be provided.
   *
   * HTTP Mapping: 520 Unknown Error
   */
  Unknown = 'UNKNOWN'
}

export type Groupset = {
  __typename?: 'Groupset';
  brand: GroupsetBrand;
  isElectronic: Scalars['Boolean']['output'];
  name: Scalars['String']['output'];
};

/**  Equipment */
export enum GroupsetBrand {
  Campagnolo = 'CAMPAGNOLO',
  Shimano = 'SHIMANO',
  Sram = 'SRAM'
}

/**
 * If multiple fields are specified, they will be combined with AND logic. If no fields are
 * specified, the filter will be treated as "match all".
 */
export type GroupsetFilterInput = {
  /** If specified, return only groupsets that belong to the provided brand */
  brand?: InputMaybe<GroupsetBrand>;
  isElectronic?: InputMaybe<Scalars['Boolean']['input']>;
  /** If specified, return only groupsets with a name that matches the string filter */
  name?: InputMaybe<StringFilterInput>;
};

/**
 * If multiple fields are specified, they will be combined with OR logic. If no fields are specified,
 * the filter will be treated as "match all".
 */
export type IntegerFilterInput = {
  equals?: InputMaybe<Scalars['Int']['input']>;
  in?: InputMaybe<Array<Scalars['Int']['input']>>;
};

/**  Models */
export type Model = {
  __typename?: 'Model';
  brand: BikeBrand;
  id: Scalars['ID']['output'];
  modelYear: Scalars['Int']['output'];
  name: Scalars['String']['output'];
};

/**
 * If multiple fields are specified, they will be combined with AND logic. If no fields are
 * specified, the filter will be treated as "match all".
 */
export type ModelFilterInput = {
  /** If specified, return only models with a brand name that matches the string filter */
  brandName?: InputMaybe<StringFilterInput>;
  /** If specified, return only models with a model year that matches the integer filter */
  modelYear?: InputMaybe<IntegerFilterInput>;
  /** If specified, return only models with a name that matches the string filter */
  name?: InputMaybe<StringFilterInput>;
};

export type Mutation = {
  __typename?: 'Mutation';
  createBike?: Maybe<Bike>;
  createBikeBrand?: Maybe<BikeBrand>;
  createGroupset?: Maybe<Groupset>;
  createModel?: Maybe<Model>;
  /** - @return the name of the successfully deleted bike */
  deleteBike?: Maybe<Scalars['ID']['output']>;
  /** - @return the name of the successfully deleted bike brand */
  deleteBikeBrand?: Maybe<Scalars['String']['output']>;
  /** - @return the name of the successfully deleted groupset */
  deleteGroupset?: Maybe<Scalars['String']['output']>;
  /** - @return the ID of the successfully deleted model */
  deleteModel?: Maybe<Scalars['ID']['output']>;
  updateBike?: Maybe<Bike>;
};


export type MutationCreateBikeArgs = {
  input: CreateBikeInput;
};


export type MutationCreateBikeBrandArgs = {
  input: CreateBikeBrandInput;
};


export type MutationCreateGroupsetArgs = {
  input: CreateGroupsetInput;
};


export type MutationCreateModelArgs = {
  input: CreateModelInput;
};


export type MutationDeleteBikeArgs = {
  id: Scalars['ID']['input'];
};


export type MutationDeleteBikeBrandArgs = {
  name: Scalars['String']['input'];
};


export type MutationDeleteGroupsetArgs = {
  name: Scalars['String']['input'];
};


export type MutationDeleteModelArgs = {
  id: Scalars['ID']['input'];
};


export type MutationUpdateBikeArgs = {
  input: UpdateBikeInput;
};

/**  Utils */
export enum Operator {
  And = 'AND',
  Or = 'OR'
}

/** Page object to support pagination */
export type PageInfo = {
  __typename?: 'PageInfo';
  /** Null if no data */
  endCursor?: Maybe<Cursor>;
  /**
   * Is there a next page for the direction we're paging in?
   *
   * I.e. if paging using `after`, is there another page after?
   */
  hasNextPage: Scalars['Boolean']['output'];
  /**
   * Is there a previous page for the direction we're paging in?
   *
   * I.e. if paging using `after`, is there a page before?
   */
  hasPreviousPage: Scalars['Boolean']['output'];
  /** Null if no data */
  startCursor?: Maybe<Cursor>;
};

/**  Queries and mutations */
export type Query = {
  __typename?: 'Query';
  _service: _Service;
  /** All bike brands */
  bikeBrands: Array<BikeBrand>;
  bikes: BikeConnection;
};


/**  Queries and mutations */
export type QueryBikesArgs = {
  after?: InputMaybe<CursorInput>;
  filter?: InputMaybe<BikesFilterInput>;
  first?: InputMaybe<Scalars['PositiveInt']['input']>;
};

/**
 * If multiple fields are specified, they will be combined with OR logic. If no fields are specified,
 * the filter will be treated as "match all".
 */
export type StringFilterInput = {
  /** Case in-sensitive match */
  contains?: InputMaybe<Scalars['String']['input']>;
  equals?: InputMaybe<Scalars['String']['input']>;
  in?: InputMaybe<Array<Scalars['String']['input']>>;
};

export type UpdateBikeInput = {
  bikeId: Scalars['ID']['input'];
  /**
   * If specified, there must be a groupset with this name already saved otherwise the mutation will
   * fail and an error will be raised
   */
  groupsetName?: InputMaybe<Scalars['String']['input']>;
  heroImageUrl?: InputMaybe<Scalars['Url']['input']>;
};

export type _Service = {
  __typename?: '_Service';
  sdl: Scalars['String']['output'];
};

export type BikesQueryVariables = Exact<{ [key: string]: never; }>;


export type BikesQuery = { __typename?: 'Query', bikes: { __typename?: 'BikeConnection', edges: Array<{ __typename?: 'BikeConnectionEdge', node: { __typename?: 'Bike', id: string } }> } };


export const BikesDocument = {"kind":"Document","definitions":[{"kind":"OperationDefinition","operation":"query","name":{"kind":"Name","value":"Bikes"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"bikes"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"edges"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"node"},"selectionSet":{"kind":"SelectionSet","selections":[{"kind":"Field","name":{"kind":"Name","value":"id"}}]}}]}}]}}]}}]} as unknown as DocumentNode<BikesQuery, BikesQueryVariables>;