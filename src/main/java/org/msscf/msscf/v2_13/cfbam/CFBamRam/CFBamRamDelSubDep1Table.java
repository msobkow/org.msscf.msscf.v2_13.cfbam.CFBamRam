
// Description: Java 11 in-memory RAM DbIO implementation for DelSubDep1.

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
 *	CFBamRamDelSubDep1Table in-memory RAM DbIO implementation
 *	for DelSubDep1.
 */
public class CFBamRamDelSubDep1Table
	implements ICFBamDelSubDep1Table
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamDelSubDep1Buff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamDelSubDep1Buff >();
	private Map< CFBamDelSubDep1ByDelTopDepIdxKey,
				Map< CFBamScopePKey,
					CFBamDelSubDep1Buff >> dictByDelTopDepIdx
		= new HashMap< CFBamDelSubDep1ByDelTopDepIdxKey,
				Map< CFBamScopePKey,
					CFBamDelSubDep1Buff >>();
	private Map< CFBamDelSubDep1ByUNameIdxKey,
			CFBamDelSubDep1Buff > dictByUNameIdx
		= new HashMap< CFBamDelSubDep1ByUNameIdxKey,
			CFBamDelSubDep1Buff >();

	public CFBamRamDelSubDep1Table( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createDelSubDep1( CFSecAuthorization Authorization,
		CFBamDelSubDep1Buff Buff )
	{
		final String S_ProcName = "createDelSubDep1";
		schema.getTableDelDep().createDelDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamDelSubDep1ByDelTopDepIdxKey keyDelTopDepIdx = schema.getFactoryDelSubDep1().newDelTopDepIdxKey();
		keyDelTopDepIdx.setRequiredDelTopDepTenantId( Buff.getRequiredDelTopDepTenantId() );
		keyDelTopDepIdx.setRequiredDelTopDepId( Buff.getRequiredDelTopDepId() );

		CFBamDelSubDep1ByUNameIdxKey keyUNameIdx = schema.getFactoryDelSubDep1().newUNameIdxKey();
		keyUNameIdx.setRequiredDelTopDepTenantId( Buff.getRequiredDelTopDepTenantId() );
		keyUNameIdx.setRequiredDelTopDepId( Buff.getRequiredDelTopDepId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"DelSubDep1UNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableDelDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"DelDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredDelTopDepTenantId(),
						Buff.getRequiredDelTopDepId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"DelTopDep",
						"DelTopDep",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamDelSubDep1Buff > subdictDelTopDepIdx;
		if( dictByDelTopDepIdx.containsKey( keyDelTopDepIdx ) ) {
			subdictDelTopDepIdx = dictByDelTopDepIdx.get( keyDelTopDepIdx );
		}
		else {
			subdictDelTopDepIdx = new HashMap< CFBamScopePKey, CFBamDelSubDep1Buff >();
			dictByDelTopDepIdx.put( keyDelTopDepIdx, subdictDelTopDepIdx );
		}
		subdictDelTopDepIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

	}

	public CFBamDelSubDep1Buff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep1.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamDelSubDep1Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelSubDep1Buff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep1.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamDelSubDep1Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelSubDep1Buff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamDelSubDep1.readAllDerived";
		CFBamDelSubDep1Buff[] retList = new CFBamDelSubDep1Buff[ dictByPKey.values().size() ];
		Iterator< CFBamDelSubDep1Buff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamDelSubDep1Buff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamDelSubDep1Buff> filteredList = new ArrayList<CFBamDelSubDep1Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamDelSubDep1Buff ) ) {
					filteredList.add( (CFBamDelSubDep1Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamDelSubDep1Buff[0] ) );
		}
	}

	public CFBamDelSubDep1Buff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDefSchemaIdx";
		CFBamDelDepBuff buffList[] = schema.getTableDelDep().readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamDelDepBuff buff;
			ArrayList<CFBamDelSubDep1Buff> filteredList = new ArrayList<CFBamDelSubDep1Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamDelSubDep1Buff ) ) {
					filteredList.add( (CFBamDelSubDep1Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamDelSubDep1Buff[0] ) );
		}
	}

	public CFBamDelSubDep1Buff[] readDerivedByDelDepIdx( CFSecAuthorization Authorization,
		long RelationTenantId,
		long RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readDerivedByDelDepIdx";
		CFBamDelDepBuff buffList[] = schema.getTableDelDep().readDerivedByDelDepIdx( Authorization,
			RelationTenantId,
			RelationId );
		if( buffList == null ) {
			return( null );
		}
		else {
			CFBamDelDepBuff buff;
			ArrayList<CFBamDelSubDep1Buff> filteredList = new ArrayList<CFBamDelSubDep1Buff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamDelSubDep1Buff ) ) {
					filteredList.add( (CFBamDelSubDep1Buff)buff );
				}
			}
			return( filteredList.toArray( new CFBamDelSubDep1Buff[0] ) );
		}
	}

	public CFBamDelSubDep1Buff[] readDerivedByDelTopDepIdx( CFSecAuthorization Authorization,
		long DelTopDepTenantId,
		long DelTopDepId )
	{
		final String S_ProcName = "CFBamRamDelSubDep1.readDerivedByDelTopDepIdx";
		CFBamDelSubDep1ByDelTopDepIdxKey key = schema.getFactoryDelSubDep1().newDelTopDepIdxKey();
		key.setRequiredDelTopDepTenantId( DelTopDepTenantId );
		key.setRequiredDelTopDepId( DelTopDepId );

		CFBamDelSubDep1Buff[] recArray;
		if( dictByDelTopDepIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamDelSubDep1Buff > subdictDelTopDepIdx
				= dictByDelTopDepIdx.get( key );
			recArray = new CFBamDelSubDep1Buff[ subdictDelTopDepIdx.size() ];
			Iterator< CFBamDelSubDep1Buff > iter = subdictDelTopDepIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamDelSubDep1Buff > subdictDelTopDepIdx
				= new HashMap< CFBamScopePKey, CFBamDelSubDep1Buff >();
			dictByDelTopDepIdx.put( key, subdictDelTopDepIdx );
			recArray = new CFBamDelSubDep1Buff[0];
		}
		return( recArray );
	}

	public CFBamDelSubDep1Buff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		long DelTopDepTenantId,
		long DelTopDepId,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelSubDep1.readDerivedByUNameIdx";
		CFBamDelSubDep1ByUNameIdxKey key = schema.getFactoryDelSubDep1().newUNameIdxKey();
		key.setRequiredDelTopDepTenantId( DelTopDepTenantId );
		key.setRequiredDelTopDepId( DelTopDepId );
		key.setRequiredName( Name );

		CFBamDelSubDep1Buff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelSubDep1Buff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamDelSubDep1Buff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelSubDep1Buff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamDelSubDep1.readBuff";
		CFBamDelSubDep1Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a818" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelSubDep1Buff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamDelSubDep1Buff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a818" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamDelSubDep1Buff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamDelSubDep1.readAllBuff";
		CFBamDelSubDep1Buff buff;
		ArrayList<CFBamDelSubDep1Buff> filteredList = new ArrayList<CFBamDelSubDep1Buff>();
		CFBamDelSubDep1Buff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a818" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamDelSubDep1Buff[0] ) );
	}

	public CFBamDelSubDep1Buff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamDelSubDep1Buff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamDelSubDep1Buff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamDelSubDep1Buff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamDelSubDep1Buff buff;
		ArrayList<CFBamDelSubDep1Buff> filteredList = new ArrayList<CFBamDelSubDep1Buff>();
		CFBamDelSubDep1Buff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamDelSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelSubDep1Buff[0] ) );
	}

	public CFBamDelSubDep1Buff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamDelDep.readBuffByDefSchemaIdx() ";
		CFBamDelSubDep1Buff buff;
		ArrayList<CFBamDelSubDep1Buff> filteredList = new ArrayList<CFBamDelSubDep1Buff>();
		CFBamDelSubDep1Buff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a817" ) ) {
				filteredList.add( (CFBamDelSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelSubDep1Buff[0] ) );
	}

	public CFBamDelSubDep1Buff[] readBuffByDelDepIdx( CFSecAuthorization Authorization,
		long RelationTenantId,
		long RelationId )
	{
		final String S_ProcName = "CFBamRamDelDep.readBuffByDelDepIdx() ";
		CFBamDelSubDep1Buff buff;
		ArrayList<CFBamDelSubDep1Buff> filteredList = new ArrayList<CFBamDelSubDep1Buff>();
		CFBamDelSubDep1Buff[] buffList = readDerivedByDelDepIdx( Authorization,
			RelationTenantId,
			RelationId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a817" ) ) {
				filteredList.add( (CFBamDelSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelSubDep1Buff[0] ) );
	}

	public CFBamDelSubDep1Buff[] readBuffByDelTopDepIdx( CFSecAuthorization Authorization,
		long DelTopDepTenantId,
		long DelTopDepId )
	{
		final String S_ProcName = "CFBamRamDelSubDep1.readBuffByDelTopDepIdx() ";
		CFBamDelSubDep1Buff buff;
		ArrayList<CFBamDelSubDep1Buff> filteredList = new ArrayList<CFBamDelSubDep1Buff>();
		CFBamDelSubDep1Buff[] buffList = readDerivedByDelTopDepIdx( Authorization,
			DelTopDepTenantId,
			DelTopDepId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a818" ) ) {
				filteredList.add( (CFBamDelSubDep1Buff)buff );
			}
		}
		return( filteredList.toArray( new CFBamDelSubDep1Buff[0] ) );
	}

	public CFBamDelSubDep1Buff readBuffByUNameIdx( CFSecAuthorization Authorization,
		long DelTopDepTenantId,
		long DelTopDepId,
		String Name )
	{
		final String S_ProcName = "CFBamRamDelSubDep1.readBuffByUNameIdx() ";
		CFBamDelSubDep1Buff buff = readDerivedByUNameIdx( Authorization,
			DelTopDepTenantId,
			DelTopDepId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a818" ) ) {
			return( (CFBamDelSubDep1Buff)buff );
		}
		else {
			return( null );
		}
	}

	/**
	 *	Read a page array of the specific DelSubDep1 buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argDefSchemaTenantId	The DelSubDep1 key attribute of the instance generating the id.
	 *
	 *	@param	argDefSchemaId	The DelSubDep1 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamDelSubDep1Buff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific DelSubDep1 buffer instances identified by the duplicate key DelDepIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argRelationTenantId	The DelSubDep1 key attribute of the instance generating the id.
	 *
	 *	@param	argRelationId	The DelSubDep1 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamDelSubDep1Buff[] pageBuffByDelDepIdx( CFSecAuthorization Authorization,
		long RelationTenantId,
		long RelationId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByDelDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific DelSubDep1 buffer instances identified by the duplicate key DelTopDepIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argDelTopDepTenantId	The DelSubDep1 key attribute of the instance generating the id.
	 *
	 *	@param	argDelTopDepId	The DelSubDep1 key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamDelSubDep1Buff[] pageBuffByDelTopDepIdx( CFSecAuthorization Authorization,
		long DelTopDepTenantId,
		long DelTopDepId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByDelTopDepIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateDelSubDep1( CFSecAuthorization Authorization,
		CFBamDelSubDep1Buff Buff )
	{
		schema.getTableDelDep().updateDelDep( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamDelSubDep1Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateDelSubDep1",
				"Existing record not found",
				"DelSubDep1",
				pkey );
		}
		CFBamDelSubDep1ByDelTopDepIdxKey existingKeyDelTopDepIdx = schema.getFactoryDelSubDep1().newDelTopDepIdxKey();
		existingKeyDelTopDepIdx.setRequiredDelTopDepTenantId( existing.getRequiredDelTopDepTenantId() );
		existingKeyDelTopDepIdx.setRequiredDelTopDepId( existing.getRequiredDelTopDepId() );

		CFBamDelSubDep1ByDelTopDepIdxKey newKeyDelTopDepIdx = schema.getFactoryDelSubDep1().newDelTopDepIdxKey();
		newKeyDelTopDepIdx.setRequiredDelTopDepTenantId( Buff.getRequiredDelTopDepTenantId() );
		newKeyDelTopDepIdx.setRequiredDelTopDepId( Buff.getRequiredDelTopDepId() );

		CFBamDelSubDep1ByUNameIdxKey existingKeyUNameIdx = schema.getFactoryDelSubDep1().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredDelTopDepTenantId( existing.getRequiredDelTopDepTenantId() );
		existingKeyUNameIdx.setRequiredDelTopDepId( existing.getRequiredDelTopDepId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamDelSubDep1ByUNameIdxKey newKeyUNameIdx = schema.getFactoryDelSubDep1().newUNameIdxKey();
		newKeyUNameIdx.setRequiredDelTopDepTenantId( Buff.getRequiredDelTopDepTenantId() );
		newKeyUNameIdx.setRequiredDelTopDepId( Buff.getRequiredDelTopDepId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateDelSubDep1",
					"DelSubDep1UNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableDelDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDelSubDep1",
						"Superclass",
						"SuperClass",
						"DelDep",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableDelTopDep().readDerivedByIdIdx( Authorization,
						Buff.getRequiredDelTopDepTenantId(),
						Buff.getRequiredDelTopDepId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateDelSubDep1",
						"Container",
						"DelTopDep",
						"DelTopDep",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamDelSubDep1Buff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictByDelTopDepIdx.get( existingKeyDelTopDepIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDelTopDepIdx.containsKey( newKeyDelTopDepIdx ) ) {
			subdict = dictByDelTopDepIdx.get( newKeyDelTopDepIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamDelSubDep1Buff >();
			dictByDelTopDepIdx.put( newKeyDelTopDepIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

	}

	public void deleteDelSubDep1( CFSecAuthorization Authorization,
		CFBamDelSubDep1Buff Buff )
	{
		final String S_ProcName = "CFBamRamDelSubDep1Table.deleteDelSubDep1() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamDelSubDep1Buff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteDelSubDep1",
				pkey );
		}
		// Short circuit self-referential code to prevent stack overflows
		Object arrCheckDelDep[] = schema.getTableDelSubDep2().readDerivedByContDelDep1Idx( Authorization,
						existing.getRequiredTenantId(),
						existing.getRequiredId() );
		if( arrCheckDelDep.length > 0 ) {
			schema.getTableDelSubDep2().deleteDelSubDep2ByContDelDep1Idx( Authorization,
						existing.getRequiredTenantId(),
						existing.getRequiredId() );
		}
		CFBamDelSubDep1ByDelTopDepIdxKey keyDelTopDepIdx = schema.getFactoryDelSubDep1().newDelTopDepIdxKey();
		keyDelTopDepIdx.setRequiredDelTopDepTenantId( existing.getRequiredDelTopDepTenantId() );
		keyDelTopDepIdx.setRequiredDelTopDepId( existing.getRequiredDelTopDepId() );

		CFBamDelSubDep1ByUNameIdxKey keyUNameIdx = schema.getFactoryDelSubDep1().newUNameIdxKey();
		keyUNameIdx.setRequiredDelTopDepTenantId( existing.getRequiredDelTopDepTenantId() );
		keyUNameIdx.setRequiredDelTopDepId( existing.getRequiredDelTopDepId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamDelSubDep1Buff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictByDelTopDepIdx.get( keyDelTopDepIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		schema.getTableDelDep().deleteDelDep( Authorization,
			Buff );
	}
	public void deleteDelSubDep1ByDelTopDepIdx( CFSecAuthorization Authorization,
		long argDelTopDepTenantId,
		long argDelTopDepId )
	{
		CFBamDelSubDep1ByDelTopDepIdxKey key = schema.getFactoryDelSubDep1().newDelTopDepIdxKey();
		key.setRequiredDelTopDepTenantId( argDelTopDepTenantId );
		key.setRequiredDelTopDepId( argDelTopDepId );
		deleteDelSubDep1ByDelTopDepIdx( Authorization, key );
	}

	public void deleteDelSubDep1ByDelTopDepIdx( CFSecAuthorization Authorization,
		CFBamDelSubDep1ByDelTopDepIdxKey argKey )
	{
		CFBamDelSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelSubDep1Buff> matchSet = new LinkedList<CFBamDelSubDep1Buff>();
		Iterator<CFBamDelSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteDelSubDep1( Authorization, cur );
		}
	}

	public void deleteDelSubDep1ByUNameIdx( CFSecAuthorization Authorization,
		long argDelTopDepTenantId,
		long argDelTopDepId,
		String argName )
	{
		CFBamDelSubDep1ByUNameIdxKey key = schema.getFactoryDelSubDep1().newUNameIdxKey();
		key.setRequiredDelTopDepTenantId( argDelTopDepTenantId );
		key.setRequiredDelTopDepId( argDelTopDepId );
		key.setRequiredName( argName );
		deleteDelSubDep1ByUNameIdx( Authorization, key );
	}

	public void deleteDelSubDep1ByUNameIdx( CFSecAuthorization Authorization,
		CFBamDelSubDep1ByUNameIdxKey argKey )
	{
		CFBamDelSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelSubDep1Buff> matchSet = new LinkedList<CFBamDelSubDep1Buff>();
		Iterator<CFBamDelSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteDelSubDep1( Authorization, cur );
		}
	}

	public void deleteDelSubDep1ByDefSchemaIdx( CFSecAuthorization Authorization,
		Long argDefSchemaTenantId,
		Long argDefSchemaId )
	{
		CFBamDelDepByDefSchemaIdxKey key = schema.getFactoryDelDep().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteDelSubDep1ByDefSchemaIdx( Authorization, key );
	}

	public void deleteDelSubDep1ByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamDelDepByDefSchemaIdxKey argKey )
	{
		CFBamDelSubDep1Buff cur;
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
		LinkedList<CFBamDelSubDep1Buff> matchSet = new LinkedList<CFBamDelSubDep1Buff>();
		Iterator<CFBamDelSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteDelSubDep1( Authorization, cur );
		}
	}

	public void deleteDelSubDep1ByDelDepIdx( CFSecAuthorization Authorization,
		long argRelationTenantId,
		long argRelationId )
	{
		CFBamDelDepByDelDepIdxKey key = schema.getFactoryDelDep().newDelDepIdxKey();
		key.setRequiredRelationTenantId( argRelationTenantId );
		key.setRequiredRelationId( argRelationId );
		deleteDelSubDep1ByDelDepIdx( Authorization, key );
	}

	public void deleteDelSubDep1ByDelDepIdx( CFSecAuthorization Authorization,
		CFBamDelDepByDelDepIdxKey argKey )
	{
		CFBamDelSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelSubDep1Buff> matchSet = new LinkedList<CFBamDelSubDep1Buff>();
		Iterator<CFBamDelSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteDelSubDep1( Authorization, cur );
		}
	}

	public void deleteDelSubDep1ByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deleteDelSubDep1ByIdIdx( Authorization, key );
	}

	public void deleteDelSubDep1ByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamDelSubDep1Buff cur;
		LinkedList<CFBamDelSubDep1Buff> matchSet = new LinkedList<CFBamDelSubDep1Buff>();
		Iterator<CFBamDelSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteDelSubDep1( Authorization, cur );
		}
	}

	public void deleteDelSubDep1ByTenantIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteDelSubDep1ByTenantIdx( Authorization, key );
	}

	public void deleteDelSubDep1ByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamDelSubDep1Buff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamDelSubDep1Buff> matchSet = new LinkedList<CFBamDelSubDep1Buff>();
		Iterator<CFBamDelSubDep1Buff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamDelSubDep1Buff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableDelSubDep1().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteDelSubDep1( Authorization, cur );
		}
	}

	public void releasePreparedStatements() {
	}
}
