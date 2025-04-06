
// Description: Java 11 in-memory RAM DbIO implementation for Index.

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
 *	CFBamRamIndexTable in-memory RAM DbIO implementation
 *	for Index.
 */
public class CFBamRamIndexTable
	implements ICFBamIndexTable
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamIndexBuff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamIndexBuff >();
	private Map< CFBamIndexByUNameIdxKey,
			CFBamIndexBuff > dictByUNameIdx
		= new HashMap< CFBamIndexByUNameIdxKey,
			CFBamIndexBuff >();
	private Map< CFBamIndexByIndexTenantIdxKey,
				Map< CFBamScopePKey,
					CFBamIndexBuff >> dictByIndexTenantIdx
		= new HashMap< CFBamIndexByIndexTenantIdxKey,
				Map< CFBamScopePKey,
					CFBamIndexBuff >>();
	private Map< CFBamIndexByIdxTableIdxKey,
				Map< CFBamScopePKey,
					CFBamIndexBuff >> dictByIdxTableIdx
		= new HashMap< CFBamIndexByIdxTableIdxKey,
				Map< CFBamScopePKey,
					CFBamIndexBuff >>();
	private Map< CFBamIndexByDefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamIndexBuff >> dictByDefSchemaIdx
		= new HashMap< CFBamIndexByDefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamIndexBuff >>();

	public CFBamRamIndexTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createIndex( CFSecAuthorization Authorization,
		CFBamIndexBuff Buff )
	{
		final String S_ProcName = "createIndex";
		schema.getTableScope().createScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamIndexByUNameIdxKey keyUNameIdx = schema.getFactoryIndex().newUNameIdxKey();
		keyUNameIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamIndexByIndexTenantIdxKey keyIndexTenantIdx = schema.getFactoryIndex().newIndexTenantIdxKey();
		keyIndexTenantIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		CFBamIndexByIdxTableIdxKey keyIdxTableIdx = schema.getFactoryIndex().newIdxTableIdxKey();
		keyIdxTableIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyIdxTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamIndexByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryIndex().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaTenantId( Buff.getOptionalDefSchemaTenantId() );
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"IndexUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Superclass",
						"SuperClass",
						"Scope",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Table",
						"Table",
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
						"IndexTenant",
						"Tenant",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFBamScopePKey, CFBamIndexBuff > subdictIndexTenantIdx;
		if( dictByIndexTenantIdx.containsKey( keyIndexTenantIdx ) ) {
			subdictIndexTenantIdx = dictByIndexTenantIdx.get( keyIndexTenantIdx );
		}
		else {
			subdictIndexTenantIdx = new HashMap< CFBamScopePKey, CFBamIndexBuff >();
			dictByIndexTenantIdx.put( keyIndexTenantIdx, subdictIndexTenantIdx );
		}
		subdictIndexTenantIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamIndexBuff > subdictIdxTableIdx;
		if( dictByIdxTableIdx.containsKey( keyIdxTableIdx ) ) {
			subdictIdxTableIdx = dictByIdxTableIdx.get( keyIdxTableIdx );
		}
		else {
			subdictIdxTableIdx = new HashMap< CFBamScopePKey, CFBamIndexBuff >();
			dictByIdxTableIdx.put( keyIdxTableIdx, subdictIdxTableIdx );
		}
		subdictIdxTableIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamIndexBuff > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFBamScopePKey, CFBamIndexBuff >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

	}

	public CFBamIndexBuff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamIndex.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamIndexBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexBuff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamIndex.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamIndexBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamIndex.readAllDerived";
		CFBamIndexBuff[] retList = new CFBamIndexBuff[ dictByPKey.values().size() ];
		Iterator< CFBamIndexBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamIndexBuff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamIndexBuff> filteredList = new ArrayList<CFBamIndexBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamIndexBuff ) ) {
					filteredList.add( (CFBamIndexBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamIndexBuff[0] ) );
		}
	}

	public CFBamIndexBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamIndex.readDerivedByUNameIdx";
		CFBamIndexByUNameIdxKey key = schema.getFactoryIndex().newUNameIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredTableId( TableId );
		key.setRequiredName( Name );

		CFBamIndexBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexBuff[] readDerivedByIndexTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamIndex.readDerivedByIndexTenantIdx";
		CFBamIndexByIndexTenantIdxKey key = schema.getFactoryIndex().newIndexTenantIdxKey();
		key.setRequiredTenantId( TenantId );

		CFBamIndexBuff[] recArray;
		if( dictByIndexTenantIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamIndexBuff > subdictIndexTenantIdx
				= dictByIndexTenantIdx.get( key );
			recArray = new CFBamIndexBuff[ subdictIndexTenantIdx.size() ];
			Iterator< CFBamIndexBuff > iter = subdictIndexTenantIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamIndexBuff > subdictIndexTenantIdx
				= new HashMap< CFBamScopePKey, CFBamIndexBuff >();
			dictByIndexTenantIdx.put( key, subdictIndexTenantIdx );
			recArray = new CFBamIndexBuff[0];
		}
		return( recArray );
	}

	public CFBamIndexBuff[] readDerivedByIdxTableIdx( CFSecAuthorization Authorization,
		long TenantId,
		long TableId )
	{
		final String S_ProcName = "CFBamRamIndex.readDerivedByIdxTableIdx";
		CFBamIndexByIdxTableIdxKey key = schema.getFactoryIndex().newIdxTableIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredTableId( TableId );

		CFBamIndexBuff[] recArray;
		if( dictByIdxTableIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamIndexBuff > subdictIdxTableIdx
				= dictByIdxTableIdx.get( key );
			recArray = new CFBamIndexBuff[ subdictIdxTableIdx.size() ];
			Iterator< CFBamIndexBuff > iter = subdictIdxTableIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamIndexBuff > subdictIdxTableIdx
				= new HashMap< CFBamScopePKey, CFBamIndexBuff >();
			dictByIdxTableIdx.put( key, subdictIdxTableIdx );
			recArray = new CFBamIndexBuff[0];
		}
		return( recArray );
	}

	public CFBamIndexBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamIndex.readDerivedByDefSchemaIdx";
		CFBamIndexByDefSchemaIdxKey key = schema.getFactoryIndex().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( DefSchemaTenantId );
		key.setOptionalDefSchemaId( DefSchemaId );

		CFBamIndexBuff[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamIndexBuff > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new CFBamIndexBuff[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamIndexBuff > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamIndexBuff > subdictDefSchemaIdx
				= new HashMap< CFBamScopePKey, CFBamIndexBuff >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new CFBamIndexBuff[0];
		}
		return( recArray );
	}

	public CFBamIndexBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamIndexBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexBuff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamIndex.readBuff";
		CFBamIndexBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a821" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexBuff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamIndexBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a821" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamIndex.readAllBuff";
		CFBamIndexBuff buff;
		ArrayList<CFBamIndexBuff> filteredList = new ArrayList<CFBamIndexBuff>();
		CFBamIndexBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a821" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexBuff[0] ) );
	}

	public CFBamIndexBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamIndexBuff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamIndexBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamIndexBuff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamIndexBuff buff;
		ArrayList<CFBamIndexBuff> filteredList = new ArrayList<CFBamIndexBuff>();
		CFBamIndexBuff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamIndexBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexBuff[0] ) );
	}

	public CFBamIndexBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long TableId,
		String Name )
	{
		final String S_ProcName = "CFBamRamIndex.readBuffByUNameIdx() ";
		CFBamIndexBuff buff = readDerivedByUNameIdx( Authorization,
			TenantId,
			TableId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a821" ) ) {
			return( (CFBamIndexBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamIndexBuff[] readBuffByIndexTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamIndex.readBuffByIndexTenantIdx() ";
		CFBamIndexBuff buff;
		ArrayList<CFBamIndexBuff> filteredList = new ArrayList<CFBamIndexBuff>();
		CFBamIndexBuff[] buffList = readDerivedByIndexTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a821" ) ) {
				filteredList.add( (CFBamIndexBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexBuff[0] ) );
	}

	public CFBamIndexBuff[] readBuffByIdxTableIdx( CFSecAuthorization Authorization,
		long TenantId,
		long TableId )
	{
		final String S_ProcName = "CFBamRamIndex.readBuffByIdxTableIdx() ";
		CFBamIndexBuff buff;
		ArrayList<CFBamIndexBuff> filteredList = new ArrayList<CFBamIndexBuff>();
		CFBamIndexBuff[] buffList = readDerivedByIdxTableIdx( Authorization,
			TenantId,
			TableId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a821" ) ) {
				filteredList.add( (CFBamIndexBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexBuff[0] ) );
	}

	public CFBamIndexBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamIndex.readBuffByDefSchemaIdx() ";
		CFBamIndexBuff buff;
		ArrayList<CFBamIndexBuff> filteredList = new ArrayList<CFBamIndexBuff>();
		CFBamIndexBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a821" ) ) {
				filteredList.add( (CFBamIndexBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexBuff[0] ) );
	}

	/**
	 *	Read a page array of the specific Index buffer instances identified by the duplicate key IndexTenantIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argTenantId	The Index key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamIndexBuff[] pageBuffByIndexTenantIdx( CFSecAuthorization Authorization,
		long TenantId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByIndexTenantIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific Index buffer instances identified by the duplicate key IdxTableIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argTenantId	The Index key attribute of the instance generating the id.
	 *
	 *	@param	argTableId	The Index key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamIndexBuff[] pageBuffByIdxTableIdx( CFSecAuthorization Authorization,
		long TenantId,
		long TableId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByIdxTableIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific Index buffer instances identified by the duplicate key DefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argDefSchemaTenantId	The Index key attribute of the instance generating the id.
	 *
	 *	@param	argDefSchemaId	The Index key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamIndexBuff[] pageBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByDefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	public void updateIndex( CFSecAuthorization Authorization,
		CFBamIndexBuff Buff )
	{
		schema.getTableScope().updateScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamIndexBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateIndex",
				"Existing record not found",
				"Index",
				pkey );
		}
		CFBamIndexByUNameIdxKey existingKeyUNameIdx = schema.getFactoryIndex().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamIndexByUNameIdxKey newKeyUNameIdx = schema.getFactoryIndex().newUNameIdxKey();
		newKeyUNameIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyUNameIdx.setRequiredTableId( Buff.getRequiredTableId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamIndexByIndexTenantIdxKey existingKeyIndexTenantIdx = schema.getFactoryIndex().newIndexTenantIdxKey();
		existingKeyIndexTenantIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamIndexByIndexTenantIdxKey newKeyIndexTenantIdx = schema.getFactoryIndex().newIndexTenantIdxKey();
		newKeyIndexTenantIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		CFBamIndexByIdxTableIdxKey existingKeyIdxTableIdx = schema.getFactoryIndex().newIdxTableIdxKey();
		existingKeyIdxTableIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyIdxTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamIndexByIdxTableIdxKey newKeyIdxTableIdx = schema.getFactoryIndex().newIdxTableIdxKey();
		newKeyIdxTableIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyIdxTableIdx.setRequiredTableId( Buff.getRequiredTableId() );

		CFBamIndexByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryIndex().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaTenantId( existing.getOptionalDefSchemaTenantId() );
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamIndexByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryIndex().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaTenantId( Buff.getOptionalDefSchemaTenantId() );
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateIndex",
					"IndexUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableScope().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateIndex",
						"Superclass",
						"SuperClass",
						"Scope",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTable().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredTableId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateIndex",
						"Container",
						"Table",
						"Table",
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
						"updateIndex",
						"Owner",
						"IndexTenant",
						"Tenant",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamIndexBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByIndexTenantIdx.get( existingKeyIndexTenantIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByIndexTenantIdx.containsKey( newKeyIndexTenantIdx ) ) {
			subdict = dictByIndexTenantIdx.get( newKeyIndexTenantIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamIndexBuff >();
			dictByIndexTenantIdx.put( newKeyIndexTenantIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByIdxTableIdx.get( existingKeyIdxTableIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByIdxTableIdx.containsKey( newKeyIdxTableIdx ) ) {
			subdict = dictByIdxTableIdx.get( newKeyIdxTableIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamIndexBuff >();
			dictByIdxTableIdx.put( newKeyIdxTableIdx, subdict );
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
			subdict = new HashMap< CFBamScopePKey, CFBamIndexBuff >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteIndex( CFSecAuthorization Authorization,
		CFBamIndexBuff Buff )
	{
		final String S_ProcName = "CFBamRamIndexTable.deleteIndex() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamIndexBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteIndex",
				pkey );
		}
		CFBamIndexColBuff buffDelIndexRefRelFromCols;
		CFBamIndexColBuff arrDelIndexRefRelFromCols[] = schema.getTableIndexCol().readDerivedByIndexIdx( Authorization,
			existing.getRequiredTenantId(),
			existing.getRequiredId() );
		for( int idxDelIndexRefRelFromCols = 0; idxDelIndexRefRelFromCols < arrDelIndexRefRelFromCols.length; idxDelIndexRefRelFromCols++ ) {
			buffDelIndexRefRelFromCols = arrDelIndexRefRelFromCols[idxDelIndexRefRelFromCols];
					schema.getTableRelationCol().deleteRelationColByFromColIdx( Authorization,
						buffDelIndexRefRelFromCols.getRequiredTenantId(),
						buffDelIndexRefRelFromCols.getRequiredId() );
		}
		CFBamIndexColBuff buffDelIndexRefRelToCols;
		CFBamIndexColBuff arrDelIndexRefRelToCols[] = schema.getTableIndexCol().readDerivedByIndexIdx( Authorization,
			existing.getRequiredTenantId(),
			existing.getRequiredId() );
		for( int idxDelIndexRefRelToCols = 0; idxDelIndexRefRelToCols < arrDelIndexRefRelToCols.length; idxDelIndexRefRelToCols++ ) {
			buffDelIndexRefRelToCols = arrDelIndexRefRelToCols[idxDelIndexRefRelToCols];
					schema.getTableRelationCol().deleteRelationColByToColIdx( Authorization,
						buffDelIndexRefRelToCols.getRequiredTenantId(),
						buffDelIndexRefRelToCols.getRequiredId() );
		}
					schema.getTableIndexCol().deleteIndexColByIndexIdx( Authorization,
						existing.getRequiredTenantId(),
						existing.getRequiredId() );
		CFBamIndexByUNameIdxKey keyUNameIdx = schema.getFactoryIndex().newUNameIdxKey();
		keyUNameIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyUNameIdx.setRequiredTableId( existing.getRequiredTableId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamIndexByIndexTenantIdxKey keyIndexTenantIdx = schema.getFactoryIndex().newIndexTenantIdxKey();
		keyIndexTenantIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamIndexByIdxTableIdxKey keyIdxTableIdx = schema.getFactoryIndex().newIdxTableIdxKey();
		keyIdxTableIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyIdxTableIdx.setRequiredTableId( existing.getRequiredTableId() );

		CFBamIndexByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryIndex().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaTenantId( existing.getOptionalDefSchemaTenantId() );
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		// Validate reverse foreign keys

		if( schema.getTableRelation().readDerivedByFromKeyIdx( Authorization,
					existing.getRequiredTenantId(),
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteIndex",
				"Lookup",
				"FromIndex",
				"Relation",
				pkey );
		}

		if( schema.getTableRelation().readDerivedByToKeyIdx( Authorization,
					existing.getRequiredTenantId(),
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteIndex",
				"Lookup",
				"ToIndex",
				"Relation",
				pkey );
		}

		// Delete is valid
		Map< CFBamScopePKey, CFBamIndexBuff > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByIndexTenantIdx.get( keyIndexTenantIdx );
		subdict.remove( pkey );

		subdict = dictByIdxTableIdx.get( keyIdxTableIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	public void deleteIndexByUNameIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argTableId,
		String argName )
	{
		CFBamIndexByUNameIdxKey key = schema.getFactoryIndex().newUNameIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredTableId( argTableId );
		key.setRequiredName( argName );
		deleteIndexByUNameIdx( Authorization, key );
	}

	public void deleteIndexByUNameIdx( CFSecAuthorization Authorization,
		CFBamIndexByUNameIdxKey argKey )
	{
		CFBamIndexBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexBuff> matchSet = new LinkedList<CFBamIndexBuff>();
		Iterator<CFBamIndexBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndex( Authorization, cur );
		}
	}

	public void deleteIndexByIndexTenantIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamIndexByIndexTenantIdxKey key = schema.getFactoryIndex().newIndexTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteIndexByIndexTenantIdx( Authorization, key );
	}

	public void deleteIndexByIndexTenantIdx( CFSecAuthorization Authorization,
		CFBamIndexByIndexTenantIdxKey argKey )
	{
		CFBamIndexBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexBuff> matchSet = new LinkedList<CFBamIndexBuff>();
		Iterator<CFBamIndexBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndex( Authorization, cur );
		}
	}

	public void deleteIndexByIdxTableIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argTableId )
	{
		CFBamIndexByIdxTableIdxKey key = schema.getFactoryIndex().newIdxTableIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredTableId( argTableId );
		deleteIndexByIdxTableIdx( Authorization, key );
	}

	public void deleteIndexByIdxTableIdx( CFSecAuthorization Authorization,
		CFBamIndexByIdxTableIdxKey argKey )
	{
		CFBamIndexBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexBuff> matchSet = new LinkedList<CFBamIndexBuff>();
		Iterator<CFBamIndexBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndex( Authorization, cur );
		}
	}

	public void deleteIndexByDefSchemaIdx( CFSecAuthorization Authorization,
		Long argDefSchemaTenantId,
		Long argDefSchemaId )
	{
		CFBamIndexByDefSchemaIdxKey key = schema.getFactoryIndex().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteIndexByDefSchemaIdx( Authorization, key );
	}

	public void deleteIndexByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamIndexByDefSchemaIdxKey argKey )
	{
		CFBamIndexBuff cur;
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
		LinkedList<CFBamIndexBuff> matchSet = new LinkedList<CFBamIndexBuff>();
		Iterator<CFBamIndexBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndex( Authorization, cur );
		}
	}

	public void deleteIndexByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deleteIndexByIdIdx( Authorization, key );
	}

	public void deleteIndexByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamIndexBuff cur;
		LinkedList<CFBamIndexBuff> matchSet = new LinkedList<CFBamIndexBuff>();
		Iterator<CFBamIndexBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndex( Authorization, cur );
		}
	}

	public void deleteIndexByTenantIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteIndexByTenantIdx( Authorization, key );
	}

	public void deleteIndexByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamIndexBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexBuff> matchSet = new LinkedList<CFBamIndexBuff>();
		Iterator<CFBamIndexBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndex().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndex( Authorization, cur );
		}
	}

	public void releasePreparedStatements() {
	}
}
