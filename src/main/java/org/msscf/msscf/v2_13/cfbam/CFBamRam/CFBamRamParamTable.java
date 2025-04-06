
// Description: Java 11 in-memory RAM DbIO implementation for Param.

/*
 *	org.msscf.msscf.CFBam
 *
 *	Copyright (c) 2020 Mark Stephen Sobkow
 *	
 *	MSS Code Factory CFBam 2.13 Business Application Model
 *	
 *	Copyright 2020-2021 Mark Stephen Sobkow
 *	
 *		This file is part of MSS Code Factory.
 *	
 *		MSS Code Factory is available under dual commercial license from Mark Stephen
 *		Sobkow, or under the terms of the GNU General Public License, Version 3
 *		or later.
 *	
 *	    MSS Code Factory is free software: you can redistribute it and/or modify
 *	    it under the terms of the GNU General Public License as published by
 *	    the Free Software Foundation, either version 3 of the License, or
 *	    (at your option) any later version.
 *	
 *	    MSS Code Factory is distributed in the hope that it will be useful,
 *	    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	    GNU General Public License for more details.
 *	
 *	    You should have received a copy of the GNU General Public License
 *	    along with MSS Code Factory.  If not, see <https://www.gnu.org/licenses/>.
 *	
 *	Donations to support MSS Code Factory can be made at
 *	https://www.paypal.com/paypalme2/MarkSobkow
 *	
 *	Please contact Mark Stephen Sobkow at mark.sobkow@gmail.com for commercial licensing.
 *
 *	Manufactured by MSS Code Factory 2.12
 */

package org.msscf.msscf.v2_13.cfbam.CFBamRam;

import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import org.msscf.msscf.v2_13.cflib.CFLib.*;
import org.msscf.msscf.v2_13.cfsec.CFSec.*;
import org.msscf.msscf.v2_13.cfint.CFInt.*;
import org.msscf.msscf.v2_13.cfbam.CFBam.*;
import org.msscf.msscf.v2_13.cfbam.CFBamObj.*;
import org.msscf.msscf.v2_13.cfsec.CFSecObj.*;
import org.msscf.msscf.v2_13.cfint.CFIntObj.*;
import org.msscf.msscf.v2_13.cfbam.CFBamObj.*;

/*
 *	CFBamRamParamTable in-memory RAM DbIO implementation
 *	for Param.
 */
public class CFBamRamParamTable
	implements ICFBamParamTable
{
	private ICFBamSchema schema;
	private Map< CFBamParamPKey,
				CFBamParamBuff > dictByPKey
		= new HashMap< CFBamParamPKey,
				CFBamParamBuff >();
	private Map< CFBamParamByUNameIdxKey,
			CFBamParamBuff > dictByUNameIdx
		= new HashMap< CFBamParamByUNameIdxKey,
			CFBamParamBuff >();
	private Map< CFBamParamByValTentIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >> dictByValTentIdx
		= new HashMap< CFBamParamByValTentIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >>();
	private Map< CFBamParamByServerMethodIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >> dictByServerMethodIdx
		= new HashMap< CFBamParamByServerMethodIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >>();
	private Map< CFBamParamByDefSchemaIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >> dictByDefSchemaIdx
		= new HashMap< CFBamParamByDefSchemaIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >>();
	private Map< CFBamParamByServerTypeIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >> dictByServerTypeIdx
		= new HashMap< CFBamParamByServerTypeIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >>();
	private Map< CFBamParamByPrevIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >> dictByPrevIdx
		= new HashMap< CFBamParamByPrevIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >>();
	private Map< CFBamParamByNextIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >> dictByNextIdx
		= new HashMap< CFBamParamByNextIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >>();
	private Map< CFBamParamByContPrevIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >> dictByContPrevIdx
		= new HashMap< CFBamParamByContPrevIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >>();
	private Map< CFBamParamByContNextIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >> dictByContNextIdx
		= new HashMap< CFBamParamByContNextIdxKey,
				Map< CFBamParamPKey,
					CFBamParamBuff >>();

	public CFBamRamParamTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createParam( CFSecAuthorization Authorization,
		CFBamParamBuff Buff )
	{
		final String S_ProcName = "createParam";
			CFBamParamBuff tail = null;

			CFBamParamBuff[] siblings = schema.getTableParam().readDerivedByServerMethodIdx( Authorization,
				Buff.getRequiredTenantId(),
				Buff.getRequiredServerMethodId() );
			for( int idx = 0; ( tail == null ) && ( idx < siblings.length ); idx ++ ) {
				if( ( siblings[idx].getOptionalNextTenantId() == null )
					&& ( siblings[idx].getOptionalNextId() == null ) )
				{
					tail = siblings[idx];
				}
			}
			if( tail != null ) {
				Buff.setOptionalPrevTenantId( tail.getRequiredTenantId() );
				Buff.setOptionalPrevId( tail.getRequiredId() );
			}
			else {
				Buff.setOptionalPrevTenantId( null );
				Buff.setOptionalPrevId( null );
			}
		
		CFBamParamPKey pkey = schema.getFactoryParam().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( ((CFBamRamTenantTable)schema.getTableTenant()).nextParamIdGen( Authorization,
			Buff.getRequiredTenantId() ) );
		Buff.setRequiredTenantId( pkey.getRequiredTenantId() );
		Buff.setRequiredId( pkey.getRequiredId() );
		CFBamParamByUNameIdxKey keyUNameIdx = schema.getFactoryParam().newUNameIdxKey();
		keyUNameIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyUNameIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamParamByValTentIdxKey keyValTentIdx = schema.getFactoryParam().newValTentIdxKey();
		keyValTentIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		CFBamParamByServerMethodIdxKey keyServerMethodIdx = schema.getFactoryParam().newServerMethodIdxKey();
		keyServerMethodIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyServerMethodIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );

		CFBamParamByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryParam().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaTenantId( Buff.getOptionalDefSchemaTenantId() );
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamParamByServerTypeIdxKey keyServerTypeIdx = schema.getFactoryParam().newServerTypeIdxKey();
		keyServerTypeIdx.setOptionalTypeTenantId( Buff.getOptionalTypeTenantId() );
		keyServerTypeIdx.setOptionalTypeId( Buff.getOptionalTypeId() );

		CFBamParamByPrevIdxKey keyPrevIdx = schema.getFactoryParam().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevTenantId( Buff.getOptionalPrevTenantId() );
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamParamByNextIdxKey keyNextIdx = schema.getFactoryParam().newNextIdxKey();
		keyNextIdx.setOptionalNextTenantId( Buff.getOptionalNextTenantId() );
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamParamByContPrevIdxKey keyContPrevIdx = schema.getFactoryParam().newContPrevIdxKey();
		keyContPrevIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyContPrevIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		keyContPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamParamByContNextIdxKey keyContNextIdx = schema.getFactoryParam().newContNextIdxKey();
		keyContNextIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyContNextIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		keyContNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"ParamUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredServerMethodId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"ServerMethod",
						"ServerMethod",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableTenant().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Owner",
						"Tenant",
						"Tenant",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			if( Buff.getOptionalTypeTenantId() != null ) {
				allNull = false;
			}
			if( Buff.getOptionalTypeId() != null ) {
				allNull = false;
			}
			if( ! allNull ) {
				if( null == schema.getTableValue().readDerivedByIdIdx( Authorization,
						Buff.getOptionalTypeTenantId(),
						Buff.getOptionalTypeId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"Type",
						"Value",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFBamParamPKey, CFBamParamBuff > subdictValTentIdx;
		if( dictByValTentIdx.containsKey( keyValTentIdx ) ) {
			subdictValTentIdx = dictByValTentIdx.get( keyValTentIdx );
		}
		else {
			subdictValTentIdx = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByValTentIdx.put( keyValTentIdx, subdictValTentIdx );
		}
		subdictValTentIdx.put( pkey, Buff );

		Map< CFBamParamPKey, CFBamParamBuff > subdictServerMethodIdx;
		if( dictByServerMethodIdx.containsKey( keyServerMethodIdx ) ) {
			subdictServerMethodIdx = dictByServerMethodIdx.get( keyServerMethodIdx );
		}
		else {
			subdictServerMethodIdx = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByServerMethodIdx.put( keyServerMethodIdx, subdictServerMethodIdx );
		}
		subdictServerMethodIdx.put( pkey, Buff );

		Map< CFBamParamPKey, CFBamParamBuff > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		Map< CFBamParamPKey, CFBamParamBuff > subdictServerTypeIdx;
		if( dictByServerTypeIdx.containsKey( keyServerTypeIdx ) ) {
			subdictServerTypeIdx = dictByServerTypeIdx.get( keyServerTypeIdx );
		}
		else {
			subdictServerTypeIdx = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByServerTypeIdx.put( keyServerTypeIdx, subdictServerTypeIdx );
		}
		subdictServerTypeIdx.put( pkey, Buff );

		Map< CFBamParamPKey, CFBamParamBuff > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFBamParamPKey, CFBamParamBuff > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		Map< CFBamParamPKey, CFBamParamBuff > subdictContPrevIdx;
		if( dictByContPrevIdx.containsKey( keyContPrevIdx ) ) {
			subdictContPrevIdx = dictByContPrevIdx.get( keyContPrevIdx );
		}
		else {
			subdictContPrevIdx = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByContPrevIdx.put( keyContPrevIdx, subdictContPrevIdx );
		}
		subdictContPrevIdx.put( pkey, Buff );

		Map< CFBamParamPKey, CFBamParamBuff > subdictContNextIdx;
		if( dictByContNextIdx.containsKey( keyContNextIdx ) ) {
			subdictContNextIdx = dictByContNextIdx.get( keyContNextIdx );
		}
		else {
			subdictContNextIdx = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByContNextIdx.put( keyContNextIdx, subdictContNextIdx );
		}
		subdictContNextIdx.put( pkey, Buff );

		if( tail != null ) {
			CFBamParamBuff tailEdit = schema.getFactoryParam().newBuff();
			tailEdit.set( (CFBamParamBuff)tail );
				tailEdit.setOptionalNextTenantId( Buff.getRequiredTenantId() );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
			schema.getTableParam().updateParam( Authorization, tailEdit );
		}
	}

	public CFBamParamBuff readDerived( CFSecAuthorization Authorization,
		CFBamParamPKey PKey )
	{
		final String S_ProcName = "CFBamRamParam.readDerived";
		CFBamParamPKey key = schema.getFactoryParam().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamParamBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamParamBuff lockDerived( CFSecAuthorization Authorization,
		CFBamParamPKey PKey )
	{
		final String S_ProcName = "CFBamRamParam.readDerived";
		CFBamParamPKey key = schema.getFactoryParam().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamParamBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamParamBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamParam.readAllDerived";
		CFBamParamBuff[] retList = new CFBamParamBuff[ dictByPKey.values().size() ];
		Iterator< CFBamParamBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamParamBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ServerMethodId,
		String Name )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByUNameIdx";
		CFBamParamByUNameIdxKey key = schema.getFactoryParam().newUNameIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredServerMethodId( ServerMethodId );
		key.setRequiredName( Name );

		CFBamParamBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamParamBuff[] readDerivedByValTentIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByValTentIdx";
		CFBamParamByValTentIdxKey key = schema.getFactoryParam().newValTentIdxKey();
		key.setRequiredTenantId( TenantId );

		CFBamParamBuff[] recArray;
		if( dictByValTentIdx.containsKey( key ) ) {
			Map< CFBamParamPKey, CFBamParamBuff > subdictValTentIdx
				= dictByValTentIdx.get( key );
			recArray = new CFBamParamBuff[ subdictValTentIdx.size() ];
			Iterator< CFBamParamBuff > iter = subdictValTentIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamParamPKey, CFBamParamBuff > subdictValTentIdx
				= new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByValTentIdx.put( key, subdictValTentIdx );
			recArray = new CFBamParamBuff[0];
		}
		return( recArray );
	}

	public CFBamParamBuff[] readDerivedByServerMethodIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ServerMethodId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByServerMethodIdx";
		CFBamParamByServerMethodIdxKey key = schema.getFactoryParam().newServerMethodIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredServerMethodId( ServerMethodId );

		CFBamParamBuff[] recArray;
		if( dictByServerMethodIdx.containsKey( key ) ) {
			Map< CFBamParamPKey, CFBamParamBuff > subdictServerMethodIdx
				= dictByServerMethodIdx.get( key );
			recArray = new CFBamParamBuff[ subdictServerMethodIdx.size() ];
			Iterator< CFBamParamBuff > iter = subdictServerMethodIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamParamPKey, CFBamParamBuff > subdictServerMethodIdx
				= new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByServerMethodIdx.put( key, subdictServerMethodIdx );
			recArray = new CFBamParamBuff[0];
		}
		return( recArray );
	}

	public CFBamParamBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByDefSchemaIdx";
		CFBamParamByDefSchemaIdxKey key = schema.getFactoryParam().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( DefSchemaTenantId );
		key.setOptionalDefSchemaId( DefSchemaId );

		CFBamParamBuff[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFBamParamPKey, CFBamParamBuff > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new CFBamParamBuff[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamParamBuff > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamParamPKey, CFBamParamBuff > subdictDefSchemaIdx
				= new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new CFBamParamBuff[0];
		}
		return( recArray );
	}

	public CFBamParamBuff[] readDerivedByServerTypeIdx( CFSecAuthorization Authorization,
		Long TypeTenantId,
		Long TypeId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByServerTypeIdx";
		CFBamParamByServerTypeIdxKey key = schema.getFactoryParam().newServerTypeIdxKey();
		key.setOptionalTypeTenantId( TypeTenantId );
		key.setOptionalTypeId( TypeId );

		CFBamParamBuff[] recArray;
		if( dictByServerTypeIdx.containsKey( key ) ) {
			Map< CFBamParamPKey, CFBamParamBuff > subdictServerTypeIdx
				= dictByServerTypeIdx.get( key );
			recArray = new CFBamParamBuff[ subdictServerTypeIdx.size() ];
			Iterator< CFBamParamBuff > iter = subdictServerTypeIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamParamPKey, CFBamParamBuff > subdictServerTypeIdx
				= new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByServerTypeIdx.put( key, subdictServerTypeIdx );
			recArray = new CFBamParamBuff[0];
		}
		return( recArray );
	}

	public CFBamParamBuff[] readDerivedByPrevIdx( CFSecAuthorization Authorization,
		Long PrevTenantId,
		Long PrevId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByPrevIdx";
		CFBamParamByPrevIdxKey key = schema.getFactoryParam().newPrevIdxKey();
		key.setOptionalPrevTenantId( PrevTenantId );
		key.setOptionalPrevId( PrevId );

		CFBamParamBuff[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFBamParamPKey, CFBamParamBuff > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new CFBamParamBuff[ subdictPrevIdx.size() ];
			Iterator< CFBamParamBuff > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamParamPKey, CFBamParamBuff > subdictPrevIdx
				= new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new CFBamParamBuff[0];
		}
		return( recArray );
	}

	public CFBamParamBuff[] readDerivedByNextIdx( CFSecAuthorization Authorization,
		Long NextTenantId,
		Long NextId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByNextIdx";
		CFBamParamByNextIdxKey key = schema.getFactoryParam().newNextIdxKey();
		key.setOptionalNextTenantId( NextTenantId );
		key.setOptionalNextId( NextId );

		CFBamParamBuff[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFBamParamPKey, CFBamParamBuff > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new CFBamParamBuff[ subdictNextIdx.size() ];
			Iterator< CFBamParamBuff > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamParamPKey, CFBamParamBuff > subdictNextIdx
				= new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new CFBamParamBuff[0];
		}
		return( recArray );
	}

	public CFBamParamBuff[] readDerivedByContPrevIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ServerMethodId,
		Long PrevId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByContPrevIdx";
		CFBamParamByContPrevIdxKey key = schema.getFactoryParam().newContPrevIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredServerMethodId( ServerMethodId );
		key.setOptionalPrevId( PrevId );

		CFBamParamBuff[] recArray;
		if( dictByContPrevIdx.containsKey( key ) ) {
			Map< CFBamParamPKey, CFBamParamBuff > subdictContPrevIdx
				= dictByContPrevIdx.get( key );
			recArray = new CFBamParamBuff[ subdictContPrevIdx.size() ];
			Iterator< CFBamParamBuff > iter = subdictContPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamParamPKey, CFBamParamBuff > subdictContPrevIdx
				= new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByContPrevIdx.put( key, subdictContPrevIdx );
			recArray = new CFBamParamBuff[0];
		}
		return( recArray );
	}

	public CFBamParamBuff[] readDerivedByContNextIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ServerMethodId,
		Long NextId )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByContNextIdx";
		CFBamParamByContNextIdxKey key = schema.getFactoryParam().newContNextIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredServerMethodId( ServerMethodId );
		key.setOptionalNextId( NextId );

		CFBamParamBuff[] recArray;
		if( dictByContNextIdx.containsKey( key ) ) {
			Map< CFBamParamPKey, CFBamParamBuff > subdictContNextIdx
				= dictByContNextIdx.get( key );
			recArray = new CFBamParamBuff[ subdictContNextIdx.size() ];
			Iterator< CFBamParamBuff > iter = subdictContNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamParamPKey, CFBamParamBuff > subdictContNextIdx
				= new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByContNextIdx.put( key, subdictContNextIdx );
			recArray = new CFBamParamBuff[0];
		}
		return( recArray );
	}

	public CFBamParamBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamParam.readDerivedByIdIdx() ";
		CFBamParamPKey key = schema.getFactoryParam().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamParamBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamParamBuff readBuff( CFSecAuthorization Authorization,
		CFBamParamPKey PKey )
	{
		final String S_ProcName = "CFBamRamParam.readBuff";
		CFBamParamBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a82f" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamParamBuff lockBuff( CFSecAuthorization Authorization,
		CFBamParamPKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamParamBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a82f" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamParamBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamParam.readAllBuff";
		CFBamParamBuff buff;
		ArrayList<CFBamParamBuff> filteredList = new ArrayList<CFBamParamBuff>();
		CFBamParamBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamParamBuff[0] ) );
	}

	public CFBamParamBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByIdIdx() ";
		CFBamParamBuff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
			return( (CFBamParamBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamParamBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ServerMethodId,
		String Name )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByUNameIdx() ";
		CFBamParamBuff buff = readDerivedByUNameIdx( Authorization,
			TenantId,
			ServerMethodId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
			return( (CFBamParamBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamParamBuff[] readBuffByValTentIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByValTentIdx() ";
		CFBamParamBuff buff;
		ArrayList<CFBamParamBuff> filteredList = new ArrayList<CFBamParamBuff>();
		CFBamParamBuff[] buffList = readDerivedByValTentIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (CFBamParamBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamParamBuff[0] ) );
	}

	public CFBamParamBuff[] readBuffByServerMethodIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ServerMethodId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByServerMethodIdx() ";
		CFBamParamBuff buff;
		ArrayList<CFBamParamBuff> filteredList = new ArrayList<CFBamParamBuff>();
		CFBamParamBuff[] buffList = readDerivedByServerMethodIdx( Authorization,
			TenantId,
			ServerMethodId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (CFBamParamBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamParamBuff[0] ) );
	}

	public CFBamParamBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByDefSchemaIdx() ";
		CFBamParamBuff buff;
		ArrayList<CFBamParamBuff> filteredList = new ArrayList<CFBamParamBuff>();
		CFBamParamBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (CFBamParamBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamParamBuff[0] ) );
	}

	public CFBamParamBuff[] readBuffByServerTypeIdx( CFSecAuthorization Authorization,
		Long TypeTenantId,
		Long TypeId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByServerTypeIdx() ";
		CFBamParamBuff buff;
		ArrayList<CFBamParamBuff> filteredList = new ArrayList<CFBamParamBuff>();
		CFBamParamBuff[] buffList = readDerivedByServerTypeIdx( Authorization,
			TypeTenantId,
			TypeId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (CFBamParamBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamParamBuff[0] ) );
	}

	public CFBamParamBuff[] readBuffByPrevIdx( CFSecAuthorization Authorization,
		Long PrevTenantId,
		Long PrevId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByPrevIdx() ";
		CFBamParamBuff buff;
		ArrayList<CFBamParamBuff> filteredList = new ArrayList<CFBamParamBuff>();
		CFBamParamBuff[] buffList = readDerivedByPrevIdx( Authorization,
			PrevTenantId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (CFBamParamBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamParamBuff[0] ) );
	}

	public CFBamParamBuff[] readBuffByNextIdx( CFSecAuthorization Authorization,
		Long NextTenantId,
		Long NextId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByNextIdx() ";
		CFBamParamBuff buff;
		ArrayList<CFBamParamBuff> filteredList = new ArrayList<CFBamParamBuff>();
		CFBamParamBuff[] buffList = readDerivedByNextIdx( Authorization,
			NextTenantId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (CFBamParamBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamParamBuff[0] ) );
	}

	public CFBamParamBuff[] readBuffByContPrevIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ServerMethodId,
		Long PrevId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByContPrevIdx() ";
		CFBamParamBuff buff;
		ArrayList<CFBamParamBuff> filteredList = new ArrayList<CFBamParamBuff>();
		CFBamParamBuff[] buffList = readDerivedByContPrevIdx( Authorization,
			TenantId,
			ServerMethodId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (CFBamParamBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamParamBuff[0] ) );
	}

	public CFBamParamBuff[] readBuffByContNextIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ServerMethodId,
		Long NextId )
	{
		final String S_ProcName = "CFBamRamParam.readBuffByContNextIdx() ";
		CFBamParamBuff buff;
		ArrayList<CFBamParamBuff> filteredList = new ArrayList<CFBamParamBuff>();
		CFBamParamBuff[] buffList = readDerivedByContNextIdx( Authorization,
			TenantId,
			ServerMethodId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a82f" ) ) {
				filteredList.add( (CFBamParamBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamParamBuff[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public CFBamParamBuff moveBuffUp( CFSecAuthorization Authorization,
		long TenantId,
		long Id,
		int revision )
	{
		final String S_ProcName = "moveBuffUp";

		CFBamParamBuff grandprev = null;
		CFBamParamBuff prev = null;
		CFBamParamBuff cur = null;
		CFBamParamBuff next = null;

		cur = schema.getTableParam().readDerivedByIdIdx(Authorization, TenantId, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalPrevTenantId() == null )
			&& ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamParamBuff)cur );
		}

		prev = schema.getTableParam().readDerivedByIdIdx(Authorization, cur.getOptionalPrevTenantId(), cur.getOptionalPrevId() );
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevTenantId() != null )
			&& ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = schema.getTableParam().readDerivedByIdIdx(Authorization, prev.getOptionalPrevTenantId(), prev.getOptionalPrevId() );
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextTenantId() != null )
			&& ( cur.getOptionalNextId() != null ) )
		{
			next = schema.getTableParam().readDerivedByIdIdx(Authorization, cur.getOptionalNextTenantId(), cur.getOptionalNextId() );
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next" );
			}
		}

		String classCode = prev.getClassCode();
		CFBamParamBuff newInstance;
			if( classCode.equals( "a82f" ) ) {
				newInstance = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamParamBuff editPrev = newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				newInstance = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamParamBuff editCur = newInstance;
		editCur.set( cur );

		CFBamParamBuff editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				newInstance = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandprev = newInstance;
			editGrandprev.set( grandprev );
		}

		CFBamParamBuff editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				newInstance = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editNext = newInstance;
			editNext.set( next );
		}

		if( editGrandprev != null ) {
			editGrandprev.setOptionalNextTenantId( cur.getRequiredTenantId() );
			editGrandprev.setOptionalNextId( cur.getRequiredId() );
			editCur.setOptionalPrevTenantId( grandprev.getRequiredTenantId() );
			editCur.setOptionalPrevId( grandprev.getRequiredId() );
		}
		else {
			editCur.setOptionalPrevTenantId( null );
			editCur.setOptionalPrevId( null );
		}

			editPrev.setOptionalPrevTenantId( cur.getRequiredTenantId() );
			editPrev.setOptionalPrevId( cur.getRequiredId() );

			editCur.setOptionalNextTenantId( prev.getRequiredTenantId() );
			editCur.setOptionalNextId( prev.getRequiredId() );

		if( next != null ) {
			editPrev.setOptionalNextTenantId( next.getRequiredTenantId() );
			editPrev.setOptionalNextId( next.getRequiredId() );
			editNext.setOptionalPrevTenantId( prev.getRequiredTenantId() );
			editNext.setOptionalPrevId( prev.getRequiredId() );
		}
		else {
			editPrev.setOptionalNextTenantId( null );
			editPrev.setOptionalNextId( null );
		}

		if( editGrandprev != null ) {
			classCode = editGrandprev.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamParamBuff)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public CFBamParamBuff moveBuffDown( CFSecAuthorization Authorization,
		long TenantId,
		long Id,
		int revision )
	{
		final String S_ProcName = "moveBuffDown";

		CFBamParamBuff prev = null;
		CFBamParamBuff cur = null;
		CFBamParamBuff next = null;
		CFBamParamBuff grandnext = null;

		cur = schema.getTableParam().readDerivedByIdIdx(Authorization, TenantId, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalNextTenantId() == null )
			&& ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamParamBuff)cur );
		}

		next = schema.getTableParam().readDerivedByIdIdx(Authorization, cur.getOptionalNextTenantId(), cur.getOptionalNextId() );
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.next" );
		}

		if( ( next.getOptionalNextTenantId() != null )
			&& ( next.getOptionalNextId() != null ) )
		{
			grandnext = schema.getTableParam().readDerivedByIdIdx(Authorization, next.getOptionalNextTenantId(), next.getOptionalNextId() );
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevTenantId() != null )
			&& ( cur.getOptionalPrevId() != null ) )
		{
			prev = schema.getTableParam().readDerivedByIdIdx(Authorization, cur.getOptionalPrevTenantId(), cur.getOptionalPrevId() );
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev" );
			}
		}

		String classCode = cur.getClassCode();
		CFBamParamBuff newInstance;
			if( classCode.equals( "a82f" ) ) {
				newInstance = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamParamBuff editCur = newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				newInstance = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamParamBuff editNext = newInstance;
		editNext.set( next );

		CFBamParamBuff editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				newInstance = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandnext = newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamParamBuff editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				newInstance = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editPrev = newInstance;
			editPrev.set( prev );
		}

		if( prev != null ) {
			editPrev.setOptionalNextTenantId( next.getRequiredTenantId() );
			editPrev.setOptionalNextId( next.getRequiredId() );
			editNext.setOptionalPrevTenantId( prev.getRequiredTenantId() );
			editNext.setOptionalPrevId( prev.getRequiredId() );
		}
		else {
			editNext.setOptionalPrevTenantId( null );
			editNext.setOptionalPrevId( null );
		}

			editCur.setOptionalPrevTenantId( next.getRequiredTenantId() );
			editCur.setOptionalPrevId( next.getRequiredId() );

			editNext.setOptionalNextTenantId( cur.getRequiredTenantId() );
			editNext.setOptionalNextId( cur.getRequiredId() );

		if( editGrandnext != null ) {
			editCur.setOptionalNextTenantId( grandnext.getRequiredTenantId() );
			editCur.setOptionalNextId( grandnext.getRequiredId() );
			editGrandnext.setOptionalPrevTenantId( cur.getRequiredTenantId() );
			editGrandnext.setOptionalPrevId( cur.getRequiredId() );
		}
		else {
			editCur.setOptionalNextTenantId( null );
			editCur.setOptionalNextId( null );
		}

		if( editPrev != null ) {
			classCode = editPrev.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editNext.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamParamBuff)editCur );
	}

	public void updateParam( CFSecAuthorization Authorization,
		CFBamParamBuff Buff )
	{
		CFBamParamPKey pkey = schema.getFactoryParam().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamParamBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateParam",
				"Existing record not found",
				"Param",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateParam",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamParamByUNameIdxKey existingKeyUNameIdx = schema.getFactoryParam().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyUNameIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamParamByUNameIdxKey newKeyUNameIdx = schema.getFactoryParam().newUNameIdxKey();
		newKeyUNameIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyUNameIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamParamByValTentIdxKey existingKeyValTentIdx = schema.getFactoryParam().newValTentIdxKey();
		existingKeyValTentIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamParamByValTentIdxKey newKeyValTentIdx = schema.getFactoryParam().newValTentIdxKey();
		newKeyValTentIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		CFBamParamByServerMethodIdxKey existingKeyServerMethodIdx = schema.getFactoryParam().newServerMethodIdxKey();
		existingKeyServerMethodIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyServerMethodIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );

		CFBamParamByServerMethodIdxKey newKeyServerMethodIdx = schema.getFactoryParam().newServerMethodIdxKey();
		newKeyServerMethodIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyServerMethodIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );

		CFBamParamByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryParam().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaTenantId( existing.getOptionalDefSchemaTenantId() );
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamParamByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryParam().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaTenantId( Buff.getOptionalDefSchemaTenantId() );
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamParamByServerTypeIdxKey existingKeyServerTypeIdx = schema.getFactoryParam().newServerTypeIdxKey();
		existingKeyServerTypeIdx.setOptionalTypeTenantId( existing.getOptionalTypeTenantId() );
		existingKeyServerTypeIdx.setOptionalTypeId( existing.getOptionalTypeId() );

		CFBamParamByServerTypeIdxKey newKeyServerTypeIdx = schema.getFactoryParam().newServerTypeIdxKey();
		newKeyServerTypeIdx.setOptionalTypeTenantId( Buff.getOptionalTypeTenantId() );
		newKeyServerTypeIdx.setOptionalTypeId( Buff.getOptionalTypeId() );

		CFBamParamByPrevIdxKey existingKeyPrevIdx = schema.getFactoryParam().newPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevTenantId( existing.getOptionalPrevTenantId() );
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamParamByPrevIdxKey newKeyPrevIdx = schema.getFactoryParam().newPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevTenantId( Buff.getOptionalPrevTenantId() );
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamParamByNextIdxKey existingKeyNextIdx = schema.getFactoryParam().newNextIdxKey();
		existingKeyNextIdx.setOptionalNextTenantId( existing.getOptionalNextTenantId() );
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamParamByNextIdxKey newKeyNextIdx = schema.getFactoryParam().newNextIdxKey();
		newKeyNextIdx.setOptionalNextTenantId( Buff.getOptionalNextTenantId() );
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamParamByContPrevIdxKey existingKeyContPrevIdx = schema.getFactoryParam().newContPrevIdxKey();
		existingKeyContPrevIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyContPrevIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		existingKeyContPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamParamByContPrevIdxKey newKeyContPrevIdx = schema.getFactoryParam().newContPrevIdxKey();
		newKeyContPrevIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyContPrevIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		newKeyContPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamParamByContNextIdxKey existingKeyContNextIdx = schema.getFactoryParam().newContNextIdxKey();
		existingKeyContNextIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyContNextIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		existingKeyContNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamParamByContNextIdxKey newKeyContNextIdx = schema.getFactoryParam().newContNextIdxKey();
		newKeyContNextIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyContNextIdx.setRequiredServerMethodId( Buff.getRequiredServerMethodId() );
		newKeyContNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateParam",
					"ParamUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredServerMethodId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateParam",
						"Container",
						"ServerMethod",
						"ServerMethod",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTenant().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateParam",
						"Owner",
						"Tenant",
						"Tenant",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			if( Buff.getOptionalTypeTenantId() != null ) {
				allNull = false;
			}			if( Buff.getOptionalTypeId() != null ) {
				allNull = false;
			}
			if( allNull ) {
				if( null == schema.getTableValue().readDerivedByIdIdx( Authorization,
						Buff.getOptionalTypeTenantId(),
						Buff.getOptionalTypeId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateParam",
						"Lookup",
						"Type",
						"Value",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamParamPKey, CFBamParamBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByValTentIdx.get( existingKeyValTentIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByValTentIdx.containsKey( newKeyValTentIdx ) ) {
			subdict = dictByValTentIdx.get( newKeyValTentIdx );
		}
		else {
			subdict = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByValTentIdx.put( newKeyValTentIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByServerMethodIdx.get( existingKeyServerMethodIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByServerMethodIdx.containsKey( newKeyServerMethodIdx ) ) {
			subdict = dictByServerMethodIdx.get( newKeyServerMethodIdx );
		}
		else {
			subdict = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByServerMethodIdx.put( newKeyServerMethodIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByDefSchemaIdx.get( existingKeyDefSchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDefSchemaIdx.containsKey( newKeyDefSchemaIdx ) ) {
			subdict = dictByDefSchemaIdx.get( newKeyDefSchemaIdx );
		}
		else {
			subdict = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByServerTypeIdx.get( existingKeyServerTypeIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByServerTypeIdx.containsKey( newKeyServerTypeIdx ) ) {
			subdict = dictByServerTypeIdx.get( newKeyServerTypeIdx );
		}
		else {
			subdict = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByServerTypeIdx.put( newKeyServerTypeIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByPrevIdx.get( existingKeyPrevIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPrevIdx.containsKey( newKeyPrevIdx ) ) {
			subdict = dictByPrevIdx.get( newKeyPrevIdx );
		}
		else {
			subdict = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByPrevIdx.put( newKeyPrevIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByNextIdx.get( existingKeyNextIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByNextIdx.containsKey( newKeyNextIdx ) ) {
			subdict = dictByNextIdx.get( newKeyNextIdx );
		}
		else {
			subdict = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByContPrevIdx.get( existingKeyContPrevIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByContPrevIdx.containsKey( newKeyContPrevIdx ) ) {
			subdict = dictByContPrevIdx.get( newKeyContPrevIdx );
		}
		else {
			subdict = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByContPrevIdx.put( newKeyContPrevIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByContNextIdx.get( existingKeyContNextIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByContNextIdx.containsKey( newKeyContNextIdx ) ) {
			subdict = dictByContNextIdx.get( newKeyContNextIdx );
		}
		else {
			subdict = new HashMap< CFBamParamPKey, CFBamParamBuff >();
			dictByContNextIdx.put( newKeyContNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteParam( CFSecAuthorization Authorization,
		CFBamParamBuff Buff )
	{
		final String S_ProcName = "CFBamRamParamTable.deleteParam() ";
		String classCode;
		CFBamParamPKey pkey = schema.getFactoryParam().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamParamBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteParam",
				pkey );
		}
		long varTenantId = existing.getRequiredTenantId();
		long varServerMethodId = existing.getRequiredServerMethodId();
		CFBamServerMethodBuff container = schema.getTableServerMethod().readDerivedByIdIdx( Authorization,
			varTenantId,
			varServerMethodId );
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		Long prevTenantId = existing.getOptionalPrevTenantId();
		Long prevId = existing.getOptionalPrevId();
		Long nextTenantId = existing.getOptionalNextTenantId();
		Long nextId = existing.getOptionalNextId();

		CFBamParamBuff prev = null;
		if( ( prevTenantId != null )
			&& ( prevId != null ) )
		{
			prev = schema.getTableParam().readDerivedByIdIdx( Authorization,
				prevTenantId,
				prevId );
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamParamBuff editPrev;
			classCode = prev.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				editPrev = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editPrev.set( prev );
			editPrev.setOptionalNextTenantId( nextTenantId );
			editPrev.setOptionalNextId( nextId );
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		CFBamParamBuff next = null;
		if( ( nextTenantId != null )
			&& ( nextId != null ) )
		{
			next = schema.getTableParam().readDerivedByIdIdx( Authorization,
				nextTenantId,
				nextId );
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamParamBuff editNext;
			classCode = next.getClassCode();
			if( classCode.equals( "a82f" ) ) {
				editNext = schema.getFactoryParam().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editNext.set( next );
			editNext.setOptionalPrevTenantId( prevTenantId );
			editNext.setOptionalPrevId( prevId );
			if( classCode.equals( "a82f" ) ) {
				schema.getTableParam().updateParam( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		CFBamParamByUNameIdxKey keyUNameIdx = schema.getFactoryParam().newUNameIdxKey();
		keyUNameIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyUNameIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamParamByValTentIdxKey keyValTentIdx = schema.getFactoryParam().newValTentIdxKey();
		keyValTentIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamParamByServerMethodIdxKey keyServerMethodIdx = schema.getFactoryParam().newServerMethodIdxKey();
		keyServerMethodIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyServerMethodIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );

		CFBamParamByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryParam().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaTenantId( existing.getOptionalDefSchemaTenantId() );
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamParamByServerTypeIdxKey keyServerTypeIdx = schema.getFactoryParam().newServerTypeIdxKey();
		keyServerTypeIdx.setOptionalTypeTenantId( existing.getOptionalTypeTenantId() );
		keyServerTypeIdx.setOptionalTypeId( existing.getOptionalTypeId() );

		CFBamParamByPrevIdxKey keyPrevIdx = schema.getFactoryParam().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevTenantId( existing.getOptionalPrevTenantId() );
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamParamByNextIdxKey keyNextIdx = schema.getFactoryParam().newNextIdxKey();
		keyNextIdx.setOptionalNextTenantId( existing.getOptionalNextTenantId() );
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamParamByContPrevIdxKey keyContPrevIdx = schema.getFactoryParam().newContPrevIdxKey();
		keyContPrevIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyContPrevIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		keyContPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamParamByContNextIdxKey keyContNextIdx = schema.getFactoryParam().newContNextIdxKey();
		keyContNextIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyContNextIdx.setRequiredServerMethodId( existing.getRequiredServerMethodId() );
		keyContNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamParamPKey, CFBamParamBuff > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByValTentIdx.get( keyValTentIdx );
		subdict.remove( pkey );

		subdict = dictByServerMethodIdx.get( keyServerMethodIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		subdict = dictByServerTypeIdx.get( keyServerTypeIdx );
		subdict.remove( pkey );

		subdict = dictByPrevIdx.get( keyPrevIdx );
		subdict.remove( pkey );

		subdict = dictByNextIdx.get( keyNextIdx );
		subdict.remove( pkey );

		subdict = dictByContPrevIdx.get( keyContPrevIdx );
		subdict.remove( pkey );

		subdict = dictByContNextIdx.get( keyContNextIdx );
		subdict.remove( pkey );

	}
	public void deleteParamByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamParamPKey key = schema.getFactoryParam().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deleteParamByIdIdx( Authorization, key );
	}

	public void deleteParamByIdIdx( CFSecAuthorization Authorization,
		CFBamParamPKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamParamBuff cur;
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByUNameIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argServerMethodId,
		String argName )
	{
		CFBamParamByUNameIdxKey key = schema.getFactoryParam().newUNameIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredServerMethodId( argServerMethodId );
		key.setRequiredName( argName );
		deleteParamByUNameIdx( Authorization, key );
	}

	public void deleteParamByUNameIdx( CFSecAuthorization Authorization,
		CFBamParamByUNameIdxKey argKey )
	{
		CFBamParamBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByValTentIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamParamByValTentIdxKey key = schema.getFactoryParam().newValTentIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteParamByValTentIdx( Authorization, key );
	}

	public void deleteParamByValTentIdx( CFSecAuthorization Authorization,
		CFBamParamByValTentIdxKey argKey )
	{
		CFBamParamBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByServerMethodIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argServerMethodId )
	{
		CFBamParamByServerMethodIdxKey key = schema.getFactoryParam().newServerMethodIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredServerMethodId( argServerMethodId );
		deleteParamByServerMethodIdx( Authorization, key );
	}

	public void deleteParamByServerMethodIdx( CFSecAuthorization Authorization,
		CFBamParamByServerMethodIdxKey argKey )
	{
		CFBamParamBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByDefSchemaIdx( CFSecAuthorization Authorization,
		Long argDefSchemaTenantId,
		Long argDefSchemaId )
	{
		CFBamParamByDefSchemaIdxKey key = schema.getFactoryParam().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteParamByDefSchemaIdx( Authorization, key );
	}

	public void deleteParamByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamParamByDefSchemaIdxKey argKey )
	{
		CFBamParamBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaTenantId() != null ) {
			anyNotNull = true;
		}
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByServerTypeIdx( CFSecAuthorization Authorization,
		Long argTypeTenantId,
		Long argTypeId )
	{
		CFBamParamByServerTypeIdxKey key = schema.getFactoryParam().newServerTypeIdxKey();
		key.setOptionalTypeTenantId( argTypeTenantId );
		key.setOptionalTypeId( argTypeId );
		deleteParamByServerTypeIdx( Authorization, key );
	}

	public void deleteParamByServerTypeIdx( CFSecAuthorization Authorization,
		CFBamParamByServerTypeIdxKey argKey )
	{
		CFBamParamBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalTypeTenantId() != null ) {
			anyNotNull = true;
		}
		if( argKey.getOptionalTypeId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByPrevIdx( CFSecAuthorization Authorization,
		Long argPrevTenantId,
		Long argPrevId )
	{
		CFBamParamByPrevIdxKey key = schema.getFactoryParam().newPrevIdxKey();
		key.setOptionalPrevTenantId( argPrevTenantId );
		key.setOptionalPrevId( argPrevId );
		deleteParamByPrevIdx( Authorization, key );
	}

	public void deleteParamByPrevIdx( CFSecAuthorization Authorization,
		CFBamParamByPrevIdxKey argKey )
	{
		CFBamParamBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevTenantId() != null ) {
			anyNotNull = true;
		}
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByNextIdx( CFSecAuthorization Authorization,
		Long argNextTenantId,
		Long argNextId )
	{
		CFBamParamByNextIdxKey key = schema.getFactoryParam().newNextIdxKey();
		key.setOptionalNextTenantId( argNextTenantId );
		key.setOptionalNextId( argNextId );
		deleteParamByNextIdx( Authorization, key );
	}

	public void deleteParamByNextIdx( CFSecAuthorization Authorization,
		CFBamParamByNextIdxKey argKey )
	{
		CFBamParamBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextTenantId() != null ) {
			anyNotNull = true;
		}
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByContPrevIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argServerMethodId,
		Long argPrevId )
	{
		CFBamParamByContPrevIdxKey key = schema.getFactoryParam().newContPrevIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredServerMethodId( argServerMethodId );
		key.setOptionalPrevId( argPrevId );
		deleteParamByContPrevIdx( Authorization, key );
	}

	public void deleteParamByContPrevIdx( CFSecAuthorization Authorization,
		CFBamParamByContPrevIdxKey argKey )
	{
		CFBamParamBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void deleteParamByContNextIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argServerMethodId,
		Long argNextId )
	{
		CFBamParamByContNextIdxKey key = schema.getFactoryParam().newContNextIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredServerMethodId( argServerMethodId );
		key.setOptionalNextId( argNextId );
		deleteParamByContNextIdx( Authorization, key );
	}

	public void deleteParamByContNextIdx( CFSecAuthorization Authorization,
		CFBamParamByContNextIdxKey argKey )
	{
		CFBamParamBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamParamBuff> matchSet = new LinkedList<CFBamParamBuff>();
		Iterator<CFBamParamBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamParamBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableParam().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteParam( Authorization, cur );
		}
	}

	public void releasePreparedStatements() {
	}
}
