
// Description: Java 11 in-memory RAM DbIO implementation for SchemaRef.

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
 *	CFBamRamSchemaRefTable in-memory RAM DbIO implementation
 *	for SchemaRef.
 */
public class CFBamRamSchemaRefTable
	implements ICFBamSchemaRefTable
{
	private ICFBamSchema schema;
	private Map< CFBamScopePKey,
				CFBamSchemaRefBuff > dictByPKey
		= new HashMap< CFBamScopePKey,
				CFBamSchemaRefBuff >();
	private Map< CFBamSchemaRefBySchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaRefBuff >> dictBySchemaIdx
		= new HashMap< CFBamSchemaRefBySchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaRefBuff >>();
	private Map< CFBamSchemaRefByUNameIdxKey,
			CFBamSchemaRefBuff > dictByUNameIdx
		= new HashMap< CFBamSchemaRefByUNameIdxKey,
			CFBamSchemaRefBuff >();
	private Map< CFBamSchemaRefByRefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaRefBuff >> dictByRefSchemaIdx
		= new HashMap< CFBamSchemaRefByRefSchemaIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaRefBuff >>();
	private Map< CFBamSchemaRefByPrevIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaRefBuff >> dictByPrevIdx
		= new HashMap< CFBamSchemaRefByPrevIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaRefBuff >>();
	private Map< CFBamSchemaRefByNextIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaRefBuff >> dictByNextIdx
		= new HashMap< CFBamSchemaRefByNextIdxKey,
				Map< CFBamScopePKey,
					CFBamSchemaRefBuff >>();

	public CFBamRamSchemaRefTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createSchemaRef( CFSecAuthorization Authorization,
		CFBamSchemaRefBuff Buff )
	{
		final String S_ProcName = "createSchemaRef";
		CFBamSchemaRefBuff tail = null;
		if( Buff.getClassCode().equals( "a804" ) ) {
			CFBamSchemaRefBuff[] siblings = schema.getTableSchemaRef().readDerivedBySchemaIdx( Authorization,
				Buff.getRequiredTenantId(),
				Buff.getRequiredSchemaId() );
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
		}
		schema.getTableScope().createScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setClassCode( Buff.getClassCode() );
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamSchemaRefBySchemaIdxKey keySchemaIdx = schema.getFactorySchemaRef().newSchemaIdxKey();
		keySchemaIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keySchemaIdx.setRequiredSchemaId( Buff.getRequiredSchemaId() );

		CFBamSchemaRefByUNameIdxKey keyUNameIdx = schema.getFactorySchemaRef().newUNameIdxKey();
		keyUNameIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyUNameIdx.setRequiredSchemaId( Buff.getRequiredSchemaId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamSchemaRefByRefSchemaIdxKey keyRefSchemaIdx = schema.getFactorySchemaRef().newRefSchemaIdxKey();
		keyRefSchemaIdx.setOptionalRefSchemaTenantId( Buff.getOptionalRefSchemaTenantId() );
		keyRefSchemaIdx.setOptionalRefSchemaId( Buff.getOptionalRefSchemaId() );

		CFBamSchemaRefByPrevIdxKey keyPrevIdx = schema.getFactorySchemaRef().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevTenantId( Buff.getOptionalPrevTenantId() );
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamSchemaRefByNextIdxKey keyNextIdx = schema.getFactorySchemaRef().newNextIdxKey();
		keyNextIdx.setOptionalNextTenantId( Buff.getOptionalNextTenantId() );
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"SchemaRefUNameIdx",
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
				if( null == schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredSchemaId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Schema",
						"SchemaDef",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictSchemaIdx;
		if( dictBySchemaIdx.containsKey( keySchemaIdx ) ) {
			subdictSchemaIdx = dictBySchemaIdx.get( keySchemaIdx );
		}
		else {
			subdictSchemaIdx = new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictBySchemaIdx.put( keySchemaIdx, subdictSchemaIdx );
		}
		subdictSchemaIdx.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictRefSchemaIdx;
		if( dictByRefSchemaIdx.containsKey( keyRefSchemaIdx ) ) {
			subdictRefSchemaIdx = dictByRefSchemaIdx.get( keyRefSchemaIdx );
		}
		else {
			subdictRefSchemaIdx = new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictByRefSchemaIdx.put( keyRefSchemaIdx, subdictRefSchemaIdx );
		}
		subdictRefSchemaIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		if( tail != null ) {
			String tailClassCode = tail.getClassCode();
			if( tailClassCode.equals( "a804" ) ) {
				CFBamSchemaRefBuff tailEdit = schema.getFactorySchemaRef().newBuff();
				tailEdit.set( (CFBamSchemaRefBuff)tail );
				tailEdit.setOptionalNextTenantId( Buff.getRequiredTenantId() );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
				schema.getTableSchemaRef().updateSchemaRef( Authorization, tailEdit );
			}
			else {
				throw new CFLibUsageException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode " + tailClassCode );
			}
		}
	}

	public CFBamSchemaRefBuff readDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamSchemaRefBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaRefBuff lockDerived( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerived";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamSchemaRefBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaRefBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamSchemaRef.readAllDerived";
		CFBamSchemaRefBuff[] retList = new CFBamSchemaRefBuff[ dictByPKey.values().size() ];
		Iterator< CFBamSchemaRefBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamSchemaRefBuff[] readDerivedByTenantIdx( CFSecAuthorization Authorization,
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
			ArrayList<CFBamSchemaRefBuff> filteredList = new ArrayList<CFBamSchemaRefBuff>();
			for( int idx = 0; idx < buffList.length; idx ++ ) {
				buff = buffList[idx];
				if( ( buff != null ) && ( buff instanceof CFBamSchemaRefBuff ) ) {
					filteredList.add( (CFBamSchemaRefBuff)buff );
				}
			}
			return( filteredList.toArray( new CFBamSchemaRefBuff[0] ) );
		}
	}

	public CFBamSchemaRefBuff[] readDerivedBySchemaIdx( CFSecAuthorization Authorization,
		long TenantId,
		long SchemaId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerivedBySchemaIdx";
		CFBamSchemaRefBySchemaIdxKey key = schema.getFactorySchemaRef().newSchemaIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredSchemaId( SchemaId );

		CFBamSchemaRefBuff[] recArray;
		if( dictBySchemaIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictSchemaIdx
				= dictBySchemaIdx.get( key );
			recArray = new CFBamSchemaRefBuff[ subdictSchemaIdx.size() ];
			Iterator< CFBamSchemaRefBuff > iter = subdictSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictSchemaIdx
				= new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictBySchemaIdx.put( key, subdictSchemaIdx );
			recArray = new CFBamSchemaRefBuff[0];
		}
		return( recArray );
	}

	public CFBamSchemaRefBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long SchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerivedByUNameIdx";
		CFBamSchemaRefByUNameIdxKey key = schema.getFactorySchemaRef().newUNameIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredSchemaId( SchemaId );
		key.setRequiredName( Name );

		CFBamSchemaRefBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaRefBuff[] readDerivedByRefSchemaIdx( CFSecAuthorization Authorization,
		Long RefSchemaTenantId,
		Long RefSchemaId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerivedByRefSchemaIdx";
		CFBamSchemaRefByRefSchemaIdxKey key = schema.getFactorySchemaRef().newRefSchemaIdxKey();
		key.setOptionalRefSchemaTenantId( RefSchemaTenantId );
		key.setOptionalRefSchemaId( RefSchemaId );

		CFBamSchemaRefBuff[] recArray;
		if( dictByRefSchemaIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictRefSchemaIdx
				= dictByRefSchemaIdx.get( key );
			recArray = new CFBamSchemaRefBuff[ subdictRefSchemaIdx.size() ];
			Iterator< CFBamSchemaRefBuff > iter = subdictRefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictRefSchemaIdx
				= new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictByRefSchemaIdx.put( key, subdictRefSchemaIdx );
			recArray = new CFBamSchemaRefBuff[0];
		}
		return( recArray );
	}

	public CFBamSchemaRefBuff[] readDerivedByPrevIdx( CFSecAuthorization Authorization,
		Long PrevTenantId,
		Long PrevId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerivedByPrevIdx";
		CFBamSchemaRefByPrevIdxKey key = schema.getFactorySchemaRef().newPrevIdxKey();
		key.setOptionalPrevTenantId( PrevTenantId );
		key.setOptionalPrevId( PrevId );

		CFBamSchemaRefBuff[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new CFBamSchemaRefBuff[ subdictPrevIdx.size() ];
			Iterator< CFBamSchemaRefBuff > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictPrevIdx
				= new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new CFBamSchemaRefBuff[0];
		}
		return( recArray );
	}

	public CFBamSchemaRefBuff[] readDerivedByNextIdx( CFSecAuthorization Authorization,
		Long NextTenantId,
		Long NextId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readDerivedByNextIdx";
		CFBamSchemaRefByNextIdxKey key = schema.getFactorySchemaRef().newNextIdxKey();
		key.setOptionalNextTenantId( NextTenantId );
		key.setOptionalNextId( NextId );

		CFBamSchemaRefBuff[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new CFBamSchemaRefBuff[ subdictNextIdx.size() ];
			Iterator< CFBamSchemaRefBuff > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamScopePKey, CFBamSchemaRefBuff > subdictNextIdx
				= new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new CFBamSchemaRefBuff[0];
		}
		return( recArray );
	}

	public CFBamSchemaRefBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamScope.readDerivedByIdIdx() ";
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamSchemaRefBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaRefBuff readBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readBuff";
		CFBamSchemaRefBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a804" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaRefBuff lockBuff( CFSecAuthorization Authorization,
		CFBamScopePKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamSchemaRefBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a804" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamSchemaRefBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readAllBuff";
		CFBamSchemaRefBuff buff;
		ArrayList<CFBamSchemaRefBuff> filteredList = new ArrayList<CFBamSchemaRefBuff>();
		CFBamSchemaRefBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a804" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaRefBuff[0] ) );
	}

	public CFBamSchemaRefBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByIdIdx() ";
		CFBamSchemaRefBuff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
			return( (CFBamSchemaRefBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamSchemaRefBuff[] readBuffByTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamScope.readBuffByTenantIdx() ";
		CFBamSchemaRefBuff buff;
		ArrayList<CFBamSchemaRefBuff> filteredList = new ArrayList<CFBamSchemaRefBuff>();
		CFBamSchemaRefBuff[] buffList = readDerivedByTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a801" ) ) {
				filteredList.add( (CFBamSchemaRefBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaRefBuff[0] ) );
	}

	public CFBamSchemaRefBuff[] readBuffBySchemaIdx( CFSecAuthorization Authorization,
		long TenantId,
		long SchemaId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readBuffBySchemaIdx() ";
		CFBamSchemaRefBuff buff;
		ArrayList<CFBamSchemaRefBuff> filteredList = new ArrayList<CFBamSchemaRefBuff>();
		CFBamSchemaRefBuff[] buffList = readDerivedBySchemaIdx( Authorization,
			TenantId,
			SchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a804" ) ) {
				filteredList.add( (CFBamSchemaRefBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaRefBuff[0] ) );
	}

	public CFBamSchemaRefBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long SchemaId,
		String Name )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readBuffByUNameIdx() ";
		CFBamSchemaRefBuff buff = readDerivedByUNameIdx( Authorization,
			TenantId,
			SchemaId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a804" ) ) {
			return( (CFBamSchemaRefBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamSchemaRefBuff[] readBuffByRefSchemaIdx( CFSecAuthorization Authorization,
		Long RefSchemaTenantId,
		Long RefSchemaId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readBuffByRefSchemaIdx() ";
		CFBamSchemaRefBuff buff;
		ArrayList<CFBamSchemaRefBuff> filteredList = new ArrayList<CFBamSchemaRefBuff>();
		CFBamSchemaRefBuff[] buffList = readDerivedByRefSchemaIdx( Authorization,
			RefSchemaTenantId,
			RefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a804" ) ) {
				filteredList.add( (CFBamSchemaRefBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaRefBuff[0] ) );
	}

	public CFBamSchemaRefBuff[] readBuffByPrevIdx( CFSecAuthorization Authorization,
		Long PrevTenantId,
		Long PrevId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readBuffByPrevIdx() ";
		CFBamSchemaRefBuff buff;
		ArrayList<CFBamSchemaRefBuff> filteredList = new ArrayList<CFBamSchemaRefBuff>();
		CFBamSchemaRefBuff[] buffList = readDerivedByPrevIdx( Authorization,
			PrevTenantId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a804" ) ) {
				filteredList.add( (CFBamSchemaRefBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaRefBuff[0] ) );
	}

	public CFBamSchemaRefBuff[] readBuffByNextIdx( CFSecAuthorization Authorization,
		Long NextTenantId,
		Long NextId )
	{
		final String S_ProcName = "CFBamRamSchemaRef.readBuffByNextIdx() ";
		CFBamSchemaRefBuff buff;
		ArrayList<CFBamSchemaRefBuff> filteredList = new ArrayList<CFBamSchemaRefBuff>();
		CFBamSchemaRefBuff[] buffList = readDerivedByNextIdx( Authorization,
			NextTenantId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a804" ) ) {
				filteredList.add( (CFBamSchemaRefBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamSchemaRefBuff[0] ) );
	}

	/**
	 *	Read a page array of the specific SchemaRef buffer instances identified by the duplicate key SchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argTenantId	The SchemaRef key attribute of the instance generating the id.
	 *
	 *	@param	argSchemaId	The SchemaRef key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamSchemaRefBuff[] pageBuffBySchemaIdx( CFSecAuthorization Authorization,
		long TenantId,
		long SchemaId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffBySchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific SchemaRef buffer instances identified by the duplicate key RefSchemaIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argRefSchemaTenantId	The SchemaRef key attribute of the instance generating the id.
	 *
	 *	@param	argRefSchemaId	The SchemaRef key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamSchemaRefBuff[] pageBuffByRefSchemaIdx( CFSecAuthorization Authorization,
		Long RefSchemaTenantId,
		Long RefSchemaId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByRefSchemaIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific SchemaRef buffer instances identified by the duplicate key PrevIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argPrevTenantId	The SchemaRef key attribute of the instance generating the id.
	 *
	 *	@param	argPrevId	The SchemaRef key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamSchemaRefBuff[] pageBuffByPrevIdx( CFSecAuthorization Authorization,
		Long PrevTenantId,
		Long PrevId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByPrevIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Read a page array of the specific SchemaRef buffer instances identified by the duplicate key NextIdx.
	 *
	 *	@param	Authorization	The session authorization information.
	 *
	 *	@param	argNextTenantId	The SchemaRef key attribute of the instance generating the id.
	 *
	 *	@param	argNextId	The SchemaRef key attribute of the instance generating the id.
	 *
	 *	@return An array of derived buffer instances for the specified key, potentially with 0 elements in the set.
	 *
	 *	@throws	CFLibNotSupportedException thrown by client-side implementations.
	 */
	public CFBamSchemaRefBuff[] pageBuffByNextIdx( CFSecAuthorization Authorization,
		Long NextTenantId,
		Long NextId,
		Long priorTenantId,
		Long priorId )
	{
		final String S_ProcName = "pageBuffByNextIdx";
		throw new CFLibNotImplementedYetException( getClass(), S_ProcName );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public CFBamSchemaRefBuff moveBuffUp( CFSecAuthorization Authorization,
		long TenantId,
		long Id,
		int revision )
	{
		final String S_ProcName = "moveBuffUp";

		CFBamSchemaRefBuff grandprev = null;
		CFBamSchemaRefBuff prev = null;
		CFBamSchemaRefBuff cur = null;
		CFBamSchemaRefBuff next = null;

		cur = schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, TenantId, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalPrevTenantId() == null )
			&& ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamSchemaRefBuff)cur );
		}

		prev = schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, cur.getOptionalPrevTenantId(), cur.getOptionalPrevId() );
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevTenantId() != null )
			&& ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, prev.getOptionalPrevTenantId(), prev.getOptionalPrevId() );
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextTenantId() != null )
			&& ( cur.getOptionalNextId() != null ) )
		{
			next = schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, cur.getOptionalNextTenantId(), cur.getOptionalNextId() );
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next" );
			}
		}

		String classCode = prev.getClassCode();
		CFBamSchemaRefBuff newInstance;
			if( classCode.equals( "a804" ) ) {
				newInstance = schema.getFactorySchemaRef().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamSchemaRefBuff editPrev = newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode.equals( "a804" ) ) {
				newInstance = schema.getFactorySchemaRef().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamSchemaRefBuff editCur = newInstance;
		editCur.set( cur );

		CFBamSchemaRefBuff editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode.equals( "a804" ) ) {
				newInstance = schema.getFactorySchemaRef().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandprev = newInstance;
			editGrandprev.set( grandprev );
		}

		CFBamSchemaRefBuff editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode.equals( "a804" ) ) {
				newInstance = schema.getFactorySchemaRef().newBuff();
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
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamSchemaRefBuff)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public CFBamSchemaRefBuff moveBuffDown( CFSecAuthorization Authorization,
		long TenantId,
		long Id,
		int revision )
	{
		final String S_ProcName = "moveBuffDown";

		CFBamSchemaRefBuff prev = null;
		CFBamSchemaRefBuff cur = null;
		CFBamSchemaRefBuff next = null;
		CFBamSchemaRefBuff grandnext = null;

		cur = schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, TenantId, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalNextTenantId() == null )
			&& ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamSchemaRefBuff)cur );
		}

		next = schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, cur.getOptionalNextTenantId(), cur.getOptionalNextId() );
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.next" );
		}

		if( ( next.getOptionalNextTenantId() != null )
			&& ( next.getOptionalNextId() != null ) )
		{
			grandnext = schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, next.getOptionalNextTenantId(), next.getOptionalNextId() );
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevTenantId() != null )
			&& ( cur.getOptionalPrevId() != null ) )
		{
			prev = schema.getTableSchemaRef().readDerivedByIdIdx(Authorization, cur.getOptionalPrevTenantId(), cur.getOptionalPrevId() );
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev" );
			}
		}

		String classCode = cur.getClassCode();
		CFBamSchemaRefBuff newInstance;
			if( classCode.equals( "a804" ) ) {
				newInstance = schema.getFactorySchemaRef().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamSchemaRefBuff editCur = newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode.equals( "a804" ) ) {
				newInstance = schema.getFactorySchemaRef().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamSchemaRefBuff editNext = newInstance;
		editNext.set( next );

		CFBamSchemaRefBuff editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode.equals( "a804" ) ) {
				newInstance = schema.getFactorySchemaRef().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandnext = newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamSchemaRefBuff editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode.equals( "a804" ) ) {
				newInstance = schema.getFactorySchemaRef().newBuff();
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
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editNext.getClassCode();
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamSchemaRefBuff)editCur );
	}

	public void updateSchemaRef( CFSecAuthorization Authorization,
		CFBamSchemaRefBuff Buff )
	{
		schema.getTableScope().updateScope( Authorization,
			Buff );
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamSchemaRefBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateSchemaRef",
				"Existing record not found",
				"SchemaRef",
				pkey );
		}
		CFBamSchemaRefBySchemaIdxKey existingKeySchemaIdx = schema.getFactorySchemaRef().newSchemaIdxKey();
		existingKeySchemaIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeySchemaIdx.setRequiredSchemaId( existing.getRequiredSchemaId() );

		CFBamSchemaRefBySchemaIdxKey newKeySchemaIdx = schema.getFactorySchemaRef().newSchemaIdxKey();
		newKeySchemaIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeySchemaIdx.setRequiredSchemaId( Buff.getRequiredSchemaId() );

		CFBamSchemaRefByUNameIdxKey existingKeyUNameIdx = schema.getFactorySchemaRef().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyUNameIdx.setRequiredSchemaId( existing.getRequiredSchemaId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamSchemaRefByUNameIdxKey newKeyUNameIdx = schema.getFactorySchemaRef().newUNameIdxKey();
		newKeyUNameIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyUNameIdx.setRequiredSchemaId( Buff.getRequiredSchemaId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamSchemaRefByRefSchemaIdxKey existingKeyRefSchemaIdx = schema.getFactorySchemaRef().newRefSchemaIdxKey();
		existingKeyRefSchemaIdx.setOptionalRefSchemaTenantId( existing.getOptionalRefSchemaTenantId() );
		existingKeyRefSchemaIdx.setOptionalRefSchemaId( existing.getOptionalRefSchemaId() );

		CFBamSchemaRefByRefSchemaIdxKey newKeyRefSchemaIdx = schema.getFactorySchemaRef().newRefSchemaIdxKey();
		newKeyRefSchemaIdx.setOptionalRefSchemaTenantId( Buff.getOptionalRefSchemaTenantId() );
		newKeyRefSchemaIdx.setOptionalRefSchemaId( Buff.getOptionalRefSchemaId() );

		CFBamSchemaRefByPrevIdxKey existingKeyPrevIdx = schema.getFactorySchemaRef().newPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevTenantId( existing.getOptionalPrevTenantId() );
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamSchemaRefByPrevIdxKey newKeyPrevIdx = schema.getFactorySchemaRef().newPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevTenantId( Buff.getOptionalPrevTenantId() );
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamSchemaRefByNextIdxKey existingKeyNextIdx = schema.getFactorySchemaRef().newNextIdxKey();
		existingKeyNextIdx.setOptionalNextTenantId( existing.getOptionalNextTenantId() );
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamSchemaRefByNextIdxKey newKeyNextIdx = schema.getFactorySchemaRef().newNextIdxKey();
		newKeyNextIdx.setOptionalNextTenantId( Buff.getOptionalNextTenantId() );
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateSchemaRef",
					"SchemaRefUNameIdx",
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
						"updateSchemaRef",
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
				if( null == schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredSchemaId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateSchemaRef",
						"Container",
						"Schema",
						"SchemaDef",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamScopePKey, CFBamSchemaRefBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		subdict = dictBySchemaIdx.get( existingKeySchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictBySchemaIdx.containsKey( newKeySchemaIdx ) ) {
			subdict = dictBySchemaIdx.get( newKeySchemaIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictBySchemaIdx.put( newKeySchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByRefSchemaIdx.get( existingKeyRefSchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByRefSchemaIdx.containsKey( newKeyRefSchemaIdx ) ) {
			subdict = dictByRefSchemaIdx.get( newKeyRefSchemaIdx );
		}
		else {
			subdict = new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictByRefSchemaIdx.put( newKeyRefSchemaIdx, subdict );
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
			subdict = new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
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
			subdict = new HashMap< CFBamScopePKey, CFBamSchemaRefBuff >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteSchemaRef( CFSecAuthorization Authorization,
		CFBamSchemaRefBuff Buff )
	{
		final String S_ProcName = "CFBamRamSchemaRefTable.deleteSchemaRef() ";
		String classCode;
		CFBamScopePKey pkey = schema.getFactoryScope().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamSchemaRefBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteSchemaRef",
				pkey );
		}
		long varTenantId = existing.getRequiredTenantId();
		long varSchemaId = existing.getRequiredSchemaId();
		CFBamSchemaDefBuff container = schema.getTableSchemaDef().readDerivedByIdIdx( Authorization,
			varTenantId,
			varSchemaId );
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

		CFBamSchemaRefBuff prev = null;
		if( ( prevTenantId != null )
			&& ( prevId != null ) )
		{
			prev = schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				prevTenantId,
				prevId );
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamSchemaRefBuff editPrev;
			classCode = prev.getClassCode();
			if( classCode.equals( "a804" ) ) {
				editPrev = schema.getFactorySchemaRef().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editPrev.set( prev );
			editPrev.setOptionalNextTenantId( nextTenantId );
			editPrev.setOptionalNextId( nextId );
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		CFBamSchemaRefBuff next = null;
		if( ( nextTenantId != null )
			&& ( nextId != null ) )
		{
			next = schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				nextTenantId,
				nextId );
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamSchemaRefBuff editNext;
			classCode = next.getClassCode();
			if( classCode.equals( "a804" ) ) {
				editNext = schema.getFactorySchemaRef().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editNext.set( next );
			editNext.setOptionalPrevTenantId( prevTenantId );
			editNext.setOptionalPrevId( prevId );
			if( classCode.equals( "a804" ) ) {
				schema.getTableSchemaRef().updateSchemaRef( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		CFBamSchemaRefBySchemaIdxKey keySchemaIdx = schema.getFactorySchemaRef().newSchemaIdxKey();
		keySchemaIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keySchemaIdx.setRequiredSchemaId( existing.getRequiredSchemaId() );

		CFBamSchemaRefByUNameIdxKey keyUNameIdx = schema.getFactorySchemaRef().newUNameIdxKey();
		keyUNameIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyUNameIdx.setRequiredSchemaId( existing.getRequiredSchemaId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamSchemaRefByRefSchemaIdxKey keyRefSchemaIdx = schema.getFactorySchemaRef().newRefSchemaIdxKey();
		keyRefSchemaIdx.setOptionalRefSchemaTenantId( existing.getOptionalRefSchemaTenantId() );
		keyRefSchemaIdx.setOptionalRefSchemaId( existing.getOptionalRefSchemaId() );

		CFBamSchemaRefByPrevIdxKey keyPrevIdx = schema.getFactorySchemaRef().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevTenantId( existing.getOptionalPrevTenantId() );
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamSchemaRefByNextIdxKey keyNextIdx = schema.getFactorySchemaRef().newNextIdxKey();
		keyNextIdx.setOptionalNextTenantId( existing.getOptionalNextTenantId() );
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		// Delete is valid
		Map< CFBamScopePKey, CFBamSchemaRefBuff > subdict;

		dictByPKey.remove( pkey );

		subdict = dictBySchemaIdx.get( keySchemaIdx );
		subdict.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByRefSchemaIdx.get( keyRefSchemaIdx );
		subdict.remove( pkey );

		subdict = dictByPrevIdx.get( keyPrevIdx );
		subdict.remove( pkey );

		subdict = dictByNextIdx.get( keyNextIdx );
		subdict.remove( pkey );

		schema.getTableScope().deleteScope( Authorization,
			Buff );
	}
	public void deleteSchemaRefBySchemaIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argSchemaId )
	{
		CFBamSchemaRefBySchemaIdxKey key = schema.getFactorySchemaRef().newSchemaIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredSchemaId( argSchemaId );
		deleteSchemaRefBySchemaIdx( Authorization, key );
	}

	public void deleteSchemaRefBySchemaIdx( CFSecAuthorization Authorization,
		CFBamSchemaRefBySchemaIdxKey argKey )
	{
		CFBamSchemaRefBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaRefBuff> matchSet = new LinkedList<CFBamSchemaRefBuff>();
		Iterator<CFBamSchemaRefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaRef( Authorization, cur );
		}
	}

	public void deleteSchemaRefByUNameIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argSchemaId,
		String argName )
	{
		CFBamSchemaRefByUNameIdxKey key = schema.getFactorySchemaRef().newUNameIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredSchemaId( argSchemaId );
		key.setRequiredName( argName );
		deleteSchemaRefByUNameIdx( Authorization, key );
	}

	public void deleteSchemaRefByUNameIdx( CFSecAuthorization Authorization,
		CFBamSchemaRefByUNameIdxKey argKey )
	{
		CFBamSchemaRefBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaRefBuff> matchSet = new LinkedList<CFBamSchemaRefBuff>();
		Iterator<CFBamSchemaRefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaRef( Authorization, cur );
		}
	}

	public void deleteSchemaRefByRefSchemaIdx( CFSecAuthorization Authorization,
		Long argRefSchemaTenantId,
		Long argRefSchemaId )
	{
		CFBamSchemaRefByRefSchemaIdxKey key = schema.getFactorySchemaRef().newRefSchemaIdxKey();
		key.setOptionalRefSchemaTenantId( argRefSchemaTenantId );
		key.setOptionalRefSchemaId( argRefSchemaId );
		deleteSchemaRefByRefSchemaIdx( Authorization, key );
	}

	public void deleteSchemaRefByRefSchemaIdx( CFSecAuthorization Authorization,
		CFBamSchemaRefByRefSchemaIdxKey argKey )
	{
		CFBamSchemaRefBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalRefSchemaTenantId() != null ) {
			anyNotNull = true;
		}
		if( argKey.getOptionalRefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaRefBuff> matchSet = new LinkedList<CFBamSchemaRefBuff>();
		Iterator<CFBamSchemaRefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaRef( Authorization, cur );
		}
	}

	public void deleteSchemaRefByPrevIdx( CFSecAuthorization Authorization,
		Long argPrevTenantId,
		Long argPrevId )
	{
		CFBamSchemaRefByPrevIdxKey key = schema.getFactorySchemaRef().newPrevIdxKey();
		key.setOptionalPrevTenantId( argPrevTenantId );
		key.setOptionalPrevId( argPrevId );
		deleteSchemaRefByPrevIdx( Authorization, key );
	}

	public void deleteSchemaRefByPrevIdx( CFSecAuthorization Authorization,
		CFBamSchemaRefByPrevIdxKey argKey )
	{
		CFBamSchemaRefBuff cur;
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
		LinkedList<CFBamSchemaRefBuff> matchSet = new LinkedList<CFBamSchemaRefBuff>();
		Iterator<CFBamSchemaRefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaRef( Authorization, cur );
		}
	}

	public void deleteSchemaRefByNextIdx( CFSecAuthorization Authorization,
		Long argNextTenantId,
		Long argNextId )
	{
		CFBamSchemaRefByNextIdxKey key = schema.getFactorySchemaRef().newNextIdxKey();
		key.setOptionalNextTenantId( argNextTenantId );
		key.setOptionalNextId( argNextId );
		deleteSchemaRefByNextIdx( Authorization, key );
	}

	public void deleteSchemaRefByNextIdx( CFSecAuthorization Authorization,
		CFBamSchemaRefByNextIdxKey argKey )
	{
		CFBamSchemaRefBuff cur;
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
		LinkedList<CFBamSchemaRefBuff> matchSet = new LinkedList<CFBamSchemaRefBuff>();
		Iterator<CFBamSchemaRefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaRef( Authorization, cur );
		}
	}

	public void deleteSchemaRefByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamScopePKey key = schema.getFactoryScope().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deleteSchemaRefByIdIdx( Authorization, key );
	}

	public void deleteSchemaRefByIdIdx( CFSecAuthorization Authorization,
		CFBamScopePKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamSchemaRefBuff cur;
		LinkedList<CFBamSchemaRefBuff> matchSet = new LinkedList<CFBamSchemaRefBuff>();
		Iterator<CFBamSchemaRefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaRef( Authorization, cur );
		}
	}

	public void deleteSchemaRefByTenantIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamScopeByTenantIdxKey key = schema.getFactoryScope().newTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteSchemaRefByTenantIdx( Authorization, key );
	}

	public void deleteSchemaRefByTenantIdx( CFSecAuthorization Authorization,
		CFBamScopeByTenantIdxKey argKey )
	{
		CFBamSchemaRefBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamSchemaRefBuff> matchSet = new LinkedList<CFBamSchemaRefBuff>();
		Iterator<CFBamSchemaRefBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamSchemaRefBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableSchemaRef().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteSchemaRef( Authorization, cur );
		}
	}

	public void releasePreparedStatements() {
	}
}
