
// Description: Java 11 in-memory RAM DbIO implementation for ClearSubDep1.

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
 *	CFBamRamClearSubDep1Table in-memory RAM DbIO implementation
 *	for ClearSubDep1.
 */
public class CFBamRamClearSubDep1Table
	implements ICFBamClearSubDep1Table
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamClearSubDep1Buff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamClearSubDep1Buff >();
	private Map< CFBamClearSubDep1ByClearTopDepIdxKey,
				Map< CFBamScopePKey,
					CFBamClearSubDep1Buff >> dictByClearTopDepIdx
		= new HashMap< CFBamClearSubDep1ByClearTopDepIdxKey,
				Map< CFBamScopePKey,
					CFBamClearSubDep1Buff >>();
	private Map< CFBamClearSubDep1ByUNameIdxKey,
			CFBamClearSubDep1Buff > dictByUNameIdx
		= new HashMap< CFBamClearSubDep1ByUNameIdxKey,
			CFBamClearSubDep1Buff >();

	public CFBamRamClearSubDep1Table( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createClearSubDep1( CFSecAuthorization Authorization,
		CFBamClearSubDep1Buff Buff )
	{
		final String S_ProcName = "createClearSubDep1";
		schema.getTableClearDep().createClearDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamClearSubDep1ByClearTopDepIdxKey keyClearTopDepIdx = schema.getFactoryClearSubDep1().newClearTopDepIdxKey();
		keyClearTopDepIdx.setRequiredClearTopDepTenantId( Buff.getRequiredClearTopDepTenantId() );
		keyClearTopDepIdx.setRequiredClearTopDepId( Buff.getRequiredClearTopDepId() );

		CFBamClearSubDep1ByUNameIdxKey keyUNameIdx = schema.getFactoryClearSubDep1().newUNameIdxKey();
		keyUNameIdx.setRequiredClearTopDepTenantId( Buff.getRequiredClearTopDepTenantId() );
		keyUNameIdx.setRequiredClearTopDepId( Buff.getRequiredClearTopDepId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"ClearSubDep1UNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableClearDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"ClearDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredClearTopDepTenantId(),
						Buff.getRequiredClearTopDepId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"ClearTopDep",
						"ClearTopDep",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamClearSubDep1Buff > subdictClearTopDepIdx;
		if( dictByClearTopDepIdx.containsKey( keyClearTopDepIdx ) ) {
			subdictClearTopDepIdx = dictByClearTopDepIdx.get( keyClearTopDepIdx );
		}
		else {
			subdictClearTopDepIdx = new HashMap< CFBamScopePKey, CFBamClearSubDep1Buff >();
			dictByClearTopDepIdx.put( keyClearTopDepIdx, subdictClearTopDepIdx );
		}
		subdictClearTopDepIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

	}

	public CFBamClearSubDep1Buff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamClearSubDep1Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep1Buff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamClearSubDep1Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep1Buff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamClearSubDep1.readAllDerived";
		CFBamClearSubDep1Buff[] retList = new CFBamClearSubDep1Buff[ dictByPKey.values().size() ];
		Iterator< CFBamClearSubDep1Buff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamClearSubDep1Buff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByTenantIdx";
		CFBamScopeBuff buffList[] = schema.getTableScope().readDerivedByTenantIdx( Authorization,
			TenantId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamScopeBuff buff;
			ArrayList<CFBamClearSubDep1Buff> filteredList = new ArrayList<CFBamClearSubDep1Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamClearSubDep1Buff ) ) {
					filteredList.add( (CFBamClearSubDep1Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamClearSubDep1Buff[0] ) );
		}
	}

	public CFBamClearSubDep1Buff[] readDerivedByClearDepIdx( CFSecAuthorization Authorization,
		long TenantId,
		long RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerivedByClearDepIdx";
		CFBamClearDepBuff buffList[] = schema.getTableClearDep().readDerivedByClearDepIdx( Authorization,
			TenantId,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamClearDepBuff buff;
			ArrayList<CFBamClearSubDep1Buff> filteredList = new ArrayList<CFBamClearSubDep1Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamClearSubDep1Buff ) ) {
					filteredList.add( (CFBamClearSubDep1Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamClearSubDep1Buff[0] ) );
		}
	}

	public CFBamClearSubDep1Buff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readDerivedByDefSchemaIdx";
		CFBamClearDepBuff buffList[] = schema.getTableClearDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamClearDepBuff buff;
			ArrayList<CFBamClearSubDep1Buff> filteredList = new ArrayList<CFBamClearSubDep1Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamClearSubDep1Buff ) ) {
					filteredList.add( (CFBamClearSubDep1Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamClearSubDep1Buff[0] ) );
		}
	}

	public CFBamClearSubDep1Buff[] readDerivedByClearTopDepIdx( CFSecAuthorization Authorization,
		long ClearTopDepTenantId,
		long ClearTopDepId )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readDerivedByClearTopDepIdx";
		CFBamClearSubDep1ByClearTopDepIdxKey key = schema.getFactoryClearSubDep1().newClearTopDepIdxKey();
		key.setRequiredClearTopDepTenantId( ClearTopDepTenantId );
		key.setRequiredClearTopDepId( ClearTopDepId );

		CFBamClearSubDep1Buff[] recArray;
		if( dictByClearTopDepIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamClearSubDep1Buff > subdictClearTopDepIdx
				= dictByClearTopDepIdx.get( key );
			recArray = new CFBamClearSubDep1Buff[ subdictClearTopDepIdx.size() ];
			Iterator< CFBamClearSubDep1Buff > iter = subdictClearTopDepIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamClearSubDep1Buff > subdictClearTopDepIdx
				= new HashMap< CFBamScopePKey, CFBamClearSubDep1Buff >();
			dictByClearTopDepIdx.put( key, subdictClearTopDepIdx );
			recArray = new CFBamClearSubDep1Buff[0];
		}
		return( recArray );
	}

	public CFBamClearSubDep1Buff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		long ClearTopDepTenantId,
		long ClearTopDepId,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readDerivedByUNameIdx";
		CFBamClearSubDep1ByUNameIdxKey key = schema.getFactoryClearSubDep1().newUNameIdxKey();
		key.setRequiredClearTopDepTenantId( ClearTopDepTenantId );
		key.setRequiredClearTopDepId( ClearTopDepId );
		key.setRequiredName( Name );

		CFBamClearSubDep1Buff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep1Buff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamClearSubDep1Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep1Buff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readBuff";
		CFBamClearSubDep1Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a811" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep1Buff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamClearSubDep1Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a811" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamClearSubDep1Buff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readAllBuff";
		CFBamClearSubDep1Buff buff;
		ArrayList<CFBamClearSubDep1Buff> filteredList = new ArrayList<CFBamClearSubDep1Buff>();
		CFBamClearSubDep1Buff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a811" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep1Buff[0] ) );
	}

	public CFBamClearSubDep1Buff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamClearSubDep1Buff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamClearSubDep1Buff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamClearSubDep1Buff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamClearSubDep1Buff buff;
		ArrayList<CFBamClearSubDep1Buff> filteredList = new ArrayList<CFBamClearSubDep1Buff>();
		CFBamClearSubDep1Buff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamClearSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep1Buff[0] ) );
	}

	public CFBamClearSubDep1Buff[] readBuffByClearDepIdx( CFSecAuthorization Authorization,
		long TenantId,
		long RelationId )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuffByClearDepIdx() ";
		CFBamClearSubDep1Buff buff;
		ArrayList<CFBamClearSubDep1Buff> filteredList = new ArrayList<CFBamClearSubDep1Buff>();
		CFBamClearSubDep1Buff[] buffList = readDerivedByClearDepIdx( Authorization,
			TenantId,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a810" ) ) {
				filteredList.add( (CFBamClearSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep1Buff[0] ) );
	}

	public CFBamClearSubDep1Buff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamClearDep.readBuffByDefSchemaIdx() ";
		CFBamClearSubDep1Buff buff;
		ArrayList<CFBamClearSubDep1Buff> filteredList = new ArrayList<CFBamClearSubDep1Buff>();
		CFBamClearSubDep1Buff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a810" ) ) {
				filteredList.add( (CFBamClearSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep1Buff[0] ) );
	}

	public CFBamClearSubDep1Buff[] readBuffByClearTopDepIdx( CFSecAuthorization Authorization,
		long ClearTopDepTenantId,
		long ClearTopDepId )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readBuffByClearTopDepIdx() ";
		CFBamClearSubDep1Buff buff;
		ArrayList<CFBamClearSubDep1Buff> filteredList = new ArrayList<CFBamClearSubDep1Buff>();
		CFBamClearSubDep1Buff[] buffList = readDerivedByClearTopDepIdx( Authorization,
			ClearTopDepTenantId,
			ClearTopDepId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a811" ) ) {
				filteredList.add( (CFBamClearSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamClearSubDep1Buff[0] ) );
	}

	public CFBamClearSubDep1Buff readBuffByUNameIdx( CFSecAuthorization Authorization,
		long ClearTopDepTenantId,
		long ClearTopDepId,
		String Name )
	{
		final String S_ProcName = "CFBamRamClearSubDep1.readBuffByUNameIdx() ";
		CFBamClearSubDep1Buff buff = readDerivedByUNameIdx( Authorization,
			ClearTopDepTenantId,
			ClearTopDepId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a811" ) ) {
			return( (CFBamClearSubDep1Buff)buff );
		}
		else {
			return( null );
		}
	}

	/**
	 *	Read a page array of the specific ClearSubDep1 buffer instances identified by the duplicate key ClearDepIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argTenantId	The ClearSubDep1 key attribute of the instance generating the id.
	 *
	 *	@param	argRelationId	The ClearSubDep1 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearSubDep1Buff[] pageBuffByClearDepIdx( CFSecAuthorization Authorization,
		long TenantId,
		long RelationId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByClearDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ClearSubDep1 buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argDefSchemaTenantId	The ClearSubDep1 key attribute of the instance generating the id.
	 *
	 *	@param	argDefSchemaId	The ClearSubDep1 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearSubDep1Buff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific ClearSubDep1 buffer instances identified by the duplicate key ClearTopDepIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argClearTopDepTenantId	The ClearSubDep1 key attribute of the instance generating the id.
	 *
	 *	@param	argClearTopDepId	The ClearSubDep1 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamClearSubDep1Buff[] pageBuffByClearTopDepIdx( CFSecAuthorization Authorization,
		long ClearTopDepTenantId,
		long ClearTopDepId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByClearTopDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateClearSubDep1( CFSecAuthorization Authorization,
		CFBamClearSubDep1Buff Buff )
	{
		schema.getTableClearDep().updateClearDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamClearSubDep1Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateClearSubDep1",
				"Existing record not found",
				"ClearSubDep1",
				pkey );
		}
		CFBamClearSubDep1ByClearTopDepIdxKey existingKeyClearTopDepIdx = schema.getFactoryClearSubDep1().newClearTopDepIdxKey();
		existingKeyClearTopDepIdx.setRequiredClearTopDepTenantId( existing.getRequiredClearTopDepTenantId() );
		existingKeyClearTopDepIdx.setRequiredClearTopDepId( existing.getRequiredClearTopDepId() );

		CFBamClearSubDep1ByClearTopDepIdxKey newKeyClearTopDepIdx = schema.getFactoryClearSubDep1().newClearTopDepIdxKey();
		newKeyClearTopDepIdx.setRequiredClearTopDepTenantId( Buff.getRequiredClearTopDepTenantId() );
		newKeyClearTopDepIdx.setRequiredClearTopDepId( Buff.getRequiredClearTopDepId() );

		CFBamClearSubDep1ByUNameIdxKey existingKeyUNameIdx = schema.getFactoryClearSubDep1().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredClearTopDepTenantId( existing.getRequiredClearTopDepTenantId() );
		existingKeyUNameIdx.setRequiredClearTopDepId( existing.getRequiredClearTopDepId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamClearSubDep1ByUNameIdxKey newKeyUNameIdx = schema.getFactoryClearSubDep1().newUNameIdxKey();
		newKeyUNameIdx.setRequiredClearTopDepTenantId( Buff.getRequiredClearTopDepTenantId() );
		newKeyUNameIdx.setRequiredClearTopDepId( Buff.getRequiredClearTopDepId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateClearSubDep1",
					"ClearSubDep1UNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableClearDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateClearSubDep1",
						"Superclass",
						"SuperClass",
						"ClearDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableClearTopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredClearTopDepTenantId(),
						Buff.getRequiredClearTopDepId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateClearSubDep1",
						"Container",
						"ClearTopDep",
						"ClearTopDep",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamClearSubDep1Buff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByClearTopDepIdx.get( existingKeyClearTopDepIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByClearTopDepIdx.containsKey( newKeyClearTopDepIdx ) ) {
			subdict = dictByClearTopDepIdx.get( newKeyClearTopDepIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamClearSubDep1Buff >();
			dictByClearTopDepIdx.put( newKeyClearTopDepIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

	}

	public void deleteClearSubDep1( CFSecAuthorization Authorization,
		CFBamClearSubDep1Buff Buff )
	{
		final String S_ProcName = "CFBamRamClearSubDep1Table.deleteClearSubDep1() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamClearSubDep1Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteClearSubDep1",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckClearDep[] = schema.getTableClearSubDep2().readDerivedByClearSubDep1Idx( Authorization,
						existing.getRequiredTenantId(),
						existing.getRequiredId() );
		if( arrCheckClearDep.length > 0 ) {
			schema.getTableClearSubDep2().deleteClearSubDep2ByClearSubDep1Idx( Authorization,
						existing.getRequiredTenantId(),
						existing.getRequiredId() );
		}
		CFBamClearSubDep1ByClearTopDepIdxKey keyClearTopDepIdx = schema.getFactoryClearSubDep1().newClearTopDepIdxKey();
		keyClearTopDepIdx.setRequiredClearTopDepTenantId( existing.getRequiredClearTopDepTenantId() );
		keyClearTopDepIdx.setRequiredClearTopDepId( existing.getRequiredClearTopDepId() );

		CFBamClearSubDep1ByUNameIdxKey keyUNameIdx = schema.getFactoryClearSubDep1().newUNameIdxKey();
		keyUNameIdx.setRequiredClearTopDepTenantId( existing.getRequiredClearTopDepTenantId() );
		keyUNameIdx.setRequiredClearTopDepId( existing.getRequiredClearTopDepId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamClearSubDep1Buff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByClearTopDepIdx.get( keyClearTopDepIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTableClearDep().deleteClearDep( Authorization,
			Buff );
	}
	public void deleteClearSubDep1ByClearTopDepIdx( CFSecAuthorization Authorization,
		long argClearTopDepTenantId,
		long argClearTopDepId )
	{
		CFBamClearSubDep1ByClearTopDepIdxKey key = schema.getFactoryClearSubDep1().newClearTopDepIdxKey();
		key.setRequiredClearTopDepTenantId( argClearTopDepTenantId );
		key.setRequiredClearTopDepId( argClearTopDepId );
		deleteClearSubDep1ByClearTopDepIdx( Authorization, key );
	}

	public void deleteClearSubDep1ByClearTopDepIdx( CFSecAuthorization Authorization,
		CFBamClearSubDep1ByClearTopDepIdxKey argKey )
	{
		CFBamClearSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep1Buff> matchSet = new LinkedList<CFBamClearSubDep1Buff>();
		Iterator<CFBamClearSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteClearSubDep1( Authorization, cur );
		}
	}

	public void deleteClearSubDep1ByUNameIdx( CFSecAuthorization Authorization,
		long argClearTopDepTenantId,
		long argClearTopDepId,
		String argName )
	{
		CFBamClearSubDep1ByUNameIdxKey key = schema.getFactoryClearSubDep1().newUNameIdxKey();
		key.setRequiredClearTopDepTenantId( argClearTopDepTenantId );
		key.setRequiredClearTopDepId( argClearTopDepId );
		key.setRequiredName( argName );
		deleteClearSubDep1ByUNameIdx( Authorization, key );
	}

	public void deleteClearSubDep1ByUNameIdx( CFSecAuthorization Authorization,
		CFBamClearSubDep1ByUNameIdxKey argKey )
	{
		CFBamClearSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep1Buff> matchSet = new LinkedList<CFBamClearSubDep1Buff>();
		Iterator<CFBamClearSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteClearSubDep1( Authorization, cur );
		}
	}

	public void deleteClearSubDep1ByClearDepIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argRelationId )
	{
		CFBamClearDepByClearDepIdxKey key = schema.getFactoryClearDep().newClearDepIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredRelationId( argRelationId );
		deleteClearSubDep1ByClearDepIdx( Authorization, key );
	}

	public void deleteClearSubDep1ByClearDepIdx( CFSecAuthorization Authorization,
		CFBamClearDepByClearDepIdxKey argKey )
	{
		CFBamClearSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep1Buff> matchSet = new LinkedList<CFBamClearSubDep1Buff>();
		Iterator<CFBamClearSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteClearSubDep1( Authorization, cur );
		}
	}

	public void deleteClearSubDep1ByDefSchemaIdx( CFSecAuthorization Authorization,
		Long argDefSchemaTenantId,
		Long argDefSchemaId )
	{
		CFBamClearDepByDefSchemaIdxKey key = schema.getFactoryClearDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteClearSubDep1ByDefSchemaIdx( Authorization, key );
	}

	public void deleteClearSubDep1ByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamClearDepByDefSchemaIdxKey argKey )
	{
		CFBamClearSubDep1Buff cur;
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
		LinkedList<CFBamClearSubDep1Buff> matchSet = new LinkedList<CFBamClearSubDep1Buff>();
		Iterator<CFBamClearSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteClearSubDep1( Authorization, cur );
		}
	}

	public void deleteClearSubDep1ByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deleteClearSubDep1ByIdIdx( Authorization, key );
	}

	public void deleteClearSubDep1ByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamClearSubDep1Buff cur;
		LinkedList<CFBamClearSubDep1Buff> matchSet = new LinkedList<CFBamClearSubDep1Buff>();
		Iterator<CFBamClearSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteClearSubDep1( Authorization, cur );
		}
	}

	public void deleteClearSubDep1ByTenantIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteClearSubDep1ByTenantIdx( Authorization, key );
	}

	public void deleteClearSubDep1ByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamClearSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamClearSubDep1Buff> matchSet = new LinkedList<CFBamClearSubDep1Buff>();
		Iterator<CFBamClearSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamClearSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableClearSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteClearSubDep1( Authorization, cur );
		}
	}

	public void releasePreparedStatements() {
	}
}
